package com.beniregev.test.rule;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.beniregev.util.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

/**
 * @author Binyamin Regev
 */
public class FixtureRule implements TestRule {

    /**
     * The Test Class that this Rule is driving mocking behavior for.
     */
    private final Object testInstance;

    /**
     * Logger
     */
    private final Log log;

    /**
     * Support for the org.easymock.Mock annotation
     */
    private Set<Object> classAnnotatedMocks;

    /**
     * Support for fields annotated with both @Autowired and @Mock
     * in the case of unit tests which may be instantiated via Spring Context
     * like a controller unit test. These fields aren't instantiated when the
     * Fixture rule is first set up, but should be Autowired in before test execution.
     */
    private Set<Field> autowiredMocks;

    /**
     * A collection of the mocks created by the FixtureRule referenced methods
     * by FixtureConfig.
     */
    private Set<Object> mocks;

    /**
     * A Collection of {@link @BehaviorMocker}s from the name of the mocker to the Instance.
     */
    private Map<String, Object> behaviorMockers;

    /**
     * A Collection of Mocks that have been named that can be referenced in a FixtureConfig
     * To add flexibility for initialization.
     */
    private Map<String, Field> namedMocks;

    /**
     * Singleton Map of concrete instances of the associated classes
     * so they can be reassigned after as part of cleanup
     *
     * Entries are the Class mapped to a List where
     * List[0] is the Field object for the singleton
     * List[1] is the Concrete Instance
     * List[2] is the Mocked Instance
     */
    private Map<Class<?>, List<?>> singletons;

    /**
     * Utility for Logging messages from the executing test.
     */
    private TestDocumenter documenter;

    /**
     * Constructs the Fixture Rule, Setting the Logger to the Test Instance's class, and caches
     * any Mocking fixtures.
     * @param testInstance -
     */
    public FixtureRule(final Object testInstance) {
        super();
        this.testInstance = testInstance;
        // for test execution error reporting
        log = LogFactory.getLog(testInstance.getClass());
        cacheMockingFixtures();
    }

    /**
     * Fixture rule wraps the execution of a Junit Test with this, applying
     * any FixtureConfigs if applicable.
     * @param base -
     * @param description -
     * @return -
     */
    @Override
    public Statement apply(final Statement base, final Description description) {
        final FixtureConfig annotation = description.getAnnotation(FixtureConfig.class);

        documenter.reportStartNewTest(description);
        if (annotation != null) {
            mocks = processRuleConfig(annotation, description.getMethodName());
            return new FixtureStatement(base, mocks);
        }

        return base;
    }

    /**
     * Process the FixtureConfig annotation, by invoking each of the methods referenced in its value array against the test
     * instance.
     *
     * @param config - the FixtureConfig to apply to the test
     * @param methodName - the Name of the method invoking the rules
     * @return a collection of the results for each referenced method. This should be the mocks generated by each
     *         method, so that we can "replay" and "verify" the mocks during test execution.
     */
    public Set<Object> processRuleConfig(final FixtureConfig config, final String methodName) {
        final String[] rules = config.value();
        final Class<?>[] noargs = new Class[] {};

        final Set<Object> mocks = new HashSet<Object>();
        mocks.addAll(classAnnotatedMocks);

        if (rules != null && rules.length > 0) {
            String loggingRule = null;
            try {
                int ruleCount = 0;
                for (final String rule : rules) {
                    ruleCount++;
                    loggingRule = rule;
                    if (StringUtils.isBlank(rule)) {
                        //skip;
                        continue;
                    }
                    // determine the mocker to use from the rule
                    final Object driver = getMocker(rule);
                    final Class<?> target = driver.getClass();

                    // also rerieve the method to use.
                    final String methodSignature = parseMethodSignature(rule);
                    String fixtureMethodName = methodSignature;
                    Class<?>[] classArgs = noargs;
                    Object[] args = null;
                    // determine if the method can be invoked with no arguments
                    if (methodSignature.contains("(")) {
                        // retrieve the name of the method. Should be everything before the first "(" and
                        // should reference a method on the behavior driver instance
                        fixtureMethodName = methodSignature.substring(0, methodSignature.indexOf("("));
                        // remove the parenthesis
                        final String parameterNames = methodSignature.substring(methodSignature.indexOf("(") + 1,
                                methodSignature.indexOf(")"));

                        // build the arguments array
                        final String[] parameters = parameterNames.split(",");
                        classArgs = new Class<?>[parameters.length];
                        args = new Object[parameters.length];

                        for (int i = 0; i < parameters.length; i++) {
                            // every argument in the method signature should reference a named mock.
                            final Field field = namedMocks.get(parameters[i]);
                            if (field == null) {
                                Logger.error(log, "Rule {2}: {0} in {1} referenced a NamedMock that does not exist. "
                                        + "Make sure to annotate your Mocks.", rule, methodName, ruleCount);
                                throw new RuntimeException("Invalid Mocking Rule " + rule);

                            } else {
                                classArgs[i] = field.getType();
                                ReflectionUtils.makeAccessible(field);
                                args[i] = ReflectionUtils.getField(field, testInstance);
                            }
                        }
                    }

                    final Method m = target.getMethod(fixtureMethodName, classArgs);

                    if (m.isAnnotationPresent(MockDoc.class)) {
                        // If the method found by the FixtureConfig is Annotated with MockDoc
                        // Log the message in Mock Doc
                        final MockDoc docAnnotation = m.getAnnotation(MockDoc.class);
                        documenter.log("--- {0}. Mocking Doc: {1}", ruleCount, docAnnotation.value());
                    }

                    final Object mock = ReflectionUtils.invokeMethod(m, driver, args);
                    // if there are any objects returned by the referenced method
                    // then they should be mocks
                    if (mock instanceof List<?>) {
                        mocks.addAll((List<?>) mock);

                    } else if (mock != null) {
                        mocks.add(mock);
                    }
                }

            } catch (final Exception e) {
                Logger.error(log, e, "Processing Rule {0} Failed in Test for {1}.{2}",
                        loggingRule, testInstance.getClass(), methodName);
                Assert.fail("There was an error processing Rule " + methodName);
            }
        }

        return mocks;
    }

    /**
     * Determine the Behavior Driver to use when processing a Rule. If no driver
     * is found, default to the FixtureRule's testInstance. The Assumption here is
     * that the format of the rule will be "behaviorMockerName:methodName". If there is no
     * ':' in the rule, then the entire rule is treated as the methodName.
     * @param rule -
     * @return -
     */
    private Object getMocker(final String rule) {

        Object rtn = testInstance;
        if (rule.contains(":")) {
            final String[] driverSignature = rule.split(":", 2);
            final String driverName = driverSignature[0];
            if (behaviorMockers.containsKey(driverName)) {
                rtn = behaviorMockers.get(driverName);
            } else {
                Logger.error(log, "Behavior Driver Referenced By Rule [" + rule + "] does not exist.");
            }
        }
        return rtn;
    }

    /**
     * Parses a method signature out of the rule. The format of the method signature can be:
     * behaviorMocker:methodName
     * behaviorMocker:methodName(namedMocks...)
     *
     * where behaviorMocker: is optional. If not specified, it is assumed the method
     * exists on the testInstance itself.
     * @param rule -
     * @return the methodName(namedMocks...) portion of the rule.
     */
    private String parseMethodSignature(final String rule) {
        if (rule.contains(":")) {
            final String[] driverSignature = rule.split(":", 2);
            return driverSignature[1];
        }
        return rule;
    }

    /**
     * Behavior Drivers Store Logic for Stubbing Behavior on Mocks.
     * This is so we can create reusable engines for creating mocks.
     *
     * This utility will parse the Test Instance for all fields annotated with {@link @BehaviorMocker}
     * and store them under the name referenced in {@link @BehaviorMocker} Value
     */
    private void cacheMockingFixtures() {
        final Map<String, Object> driverMap = new HashMap<String, Object>();
        final Map<String, Field> mockMap = new HashMap<String, Field>();
        final Map<Class<?>, List<?>> singletonMap = new HashMap<>();
        final Set<Object> classMocks = new HashSet<Object>();
        final Set<Field> autowiredMocks = new HashSet<>();

        Object testSubject = null;
        for (final Field field : testInstance.getClass().getDeclaredFields()) {
            // find the testSubject if there is one
            if (field.isAnnotationPresent(TestSubject.class)) {
                // make the field accessible so we can retrieve the value via reflection.
                ReflectionUtils.makeAccessible(field);
                testSubject = ReflectionUtils.getField(field, testInstance);
                if (testSubject == null) {
                    try {
                        testSubject = field.getType().newInstance();
                        ReflectionUtils.setField(field, testInstance, testSubject);
                    }
                    catch(Exception ex) {
                        Logger.warn(log, "Could not instantiate test subject {0}, skipping", field);
                    }
                }
            }
        }

        for (final Field field : testInstance.getClass().getDeclaredFields()) {
            // Find all Fields in the TestInstance that are annotated
            // with BehaviorMocker so that we can cache them for usage
            // when parsing any @FixtureConfigs
            if (field.isAnnotationPresent(FixtureMocker.class)) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    Logger.error(log, "FixtureMocker {0} should be declared as 'static'.", field.getName());
                }

                // make the field accessible so we can retrieve the value via reflection.
                ReflectionUtils.makeAccessible(field);


                final FixtureMocker annotation = field.getAnnotation(FixtureMocker.class);
                final String name = annotation.value();
                final Object driver = ReflectionUtils.getField(field, testInstance);
                if (driver == null || StringUtils.isBlank(name)) {
                    Logger.error(log, "Could Not Parse FixtureMocker for Field {0}", field.getName());
                } else {
                    driverMap.put(name, driver);
                }
            } else if (field.isAnnotationPresent(TestFixture.class)) {
                final TestFixture annotation = field.getAnnotation(TestFixture.class);
                mockMap.put(annotation.value(), field);
            } else if (field.isAnnotationPresent(DocHandler.class)) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    Logger.error(log, "DocHandler {0} should be declared as 'static'.", field.getName());
                }

                // make the field accessible so we can retrieve the value via reflection.
                ReflectionUtils.makeAccessible(field);
                this.documenter = (TestDocumenter) ReflectionUtils.getField(field, testInstance);
            } else if (field.isAnnotationPresent(MockSingleton.class)) {
                // make the field accessible so we can retrieve the value via reflection.
                ReflectionUtils.makeAccessible(field);
                Class<?> targetClass = field.getType();
                Logger.info(log, "Setting up Singleton for {0}", targetClass);
                MockSingleton annot = field.getAnnotation(MockSingleton.class);


                // set the instance mock on the class
                MockType mockType = annot.type();
                Object mockedInstance;
                if (mockType.equals(MockType.NICE)) {
                    mockedInstance = EasyMock.createNiceMock(targetClass);
                }
                else if (mockType.equals(MockType.STRICT)) {
                    mockedInstance = EasyMock.createStrictMock(targetClass);
                }
                else {
                    mockedInstance = EasyMock.createMock(targetClass);
                }
                classMocks.add(mockedInstance);


                String fieldSingleton = annot.field();

                try {
                    Field singletonField = targetClass.getDeclaredField(fieldSingleton);
                    singletonField.setAccessible(true);
                    // remove the final modifier if its there
                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(singletonField, singletonField.getModifiers() & ~Modifier.FINAL);
                    Object concreteInstance = singletonField.get(null);
                    singletonMap.put(targetClass, Arrays.asList(singletonField, concreteInstance, mockedInstance));
                    ReflectionUtils.setField(field, testInstance, mockedInstance);

                    if (testSubject != null) {
                        injectMockIntoTestSubject(testSubject, targetClass, field, mockedInstance);
                    }
                }
                catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    Logger.info(log, e, "Could not find singleton field {0} on {1} - {2}", fieldSingleton, targetClass, e.getLocalizedMessage());
                }


            } else if (field.isAnnotationPresent(Mock.class)) {
                Object mockedInstance = null;
                // make the field accessible so we can retrieve the value via reflection.
                ReflectionUtils.makeAccessible(field);
                Class<?> targetClass = field.getType();
                if (!field.isAnnotationPresent(Autowired.class)) {
                    MockType mockType = field.getAnnotation(Mock.class).type();
                    if (mockType.equals(MockType.NICE)) {
                        mockedInstance = EasyMock.createNiceMock(targetClass);
                    }
                    else if (mockType.equals(MockType.STRICT)) {
                        mockedInstance = EasyMock.createStrictMock(targetClass);
                    }
                    else {
                        mockedInstance = EasyMock.createMock(targetClass);
                    }

                    ReflectionUtils.setField(field, testInstance, mockedInstance);
                    classMocks.add(mockedInstance);
                }
                else {
                    // Fields that are annotated with @Autowired and @Mock aren't instantiated yet, so cache for later
                    // replaying and verification
                    autowiredMocks.add(field);
                }
                if (testSubject != null) {
                    injectMockIntoTestSubject(testSubject, targetClass, field, mockedInstance);
                }
            }
        }

        if (this.documenter == null) {
            this.documenter = TestDocumenterFactory.getDefault(testInstance.getClass());
        }
        this.behaviorMockers = Collections.unmodifiableMap(driverMap);
        this.namedMocks = Collections.unmodifiableMap(mockMap);
        this.classAnnotatedMocks = Collections.unmodifiableSet(classMocks);
        this.singletons = Collections.unmodifiableMap(singletonMap);
        this.autowiredMocks = Collections.unmodifiableSet(autowiredMocks);
    }

    private void injectMockIntoTestSubject(Object testSubject, Class<?> targetClass, Field field, Object mockedInstance) {
        if (testSubject != null) {
            Class<?> testSubjectClass = testSubject.getClass();
            List<Field> fieldCandidates = new ArrayList<>();
            for (final Field subjectField : testSubjectClass.getDeclaredFields()) {
                if (subjectField.getType().equals(targetClass)) {
                    Logger.info(log, "Found Field Injectable {0} on {1} by type", subjectField, testSubject);
                    fieldCandidates.add(subjectField);
                }
                else if (subjectField.getName().equals(field.getName())) {
                    Logger.info(log, "Found Field Injectable {0} on {1} by name", subjectField, testSubject);
                    fieldCandidates.add(subjectField);
                }
            }
            if (!fieldCandidates.isEmpty()) {
                Field targetField = fieldCandidates.get(0);
                ReflectionUtils.makeAccessible(targetField);
                Logger.info(log, "Injecting Mock {0} into field {1} on {2}", mockedInstance, targetField, testSubject);
                ReflectionUtils.setField(fieldCandidates.get(0), testSubject, mockedInstance);
            }
        }
    }

    /**
     * A statement that will replay all the generated mocks before executing the test, as well as verify the mocks after
     * test execution
     *
     * @author leon.kay
     */
    public class FixtureStatement extends Statement {

        /**
         * Collection of Mocks generated by the FixtureConfig that need to be Replayed
         * before they can be used.
         */
        private final Collection<Object> mocks;

        /**
         * Base Statement.
         */
        private final Statement base;

        /**
         * @param base -
         * @param mocks -
         */
        public FixtureStatement(final Statement base, final Collection<Object> mocks) {
            super();
            this.base = base;
            this.mocks = mocks;
        }

        /*
         * (non-Javadoc)
         * @see org.junit.runners.model.Statement#evaluate()
         */
        @Override
        public void evaluate() throws Throwable {
            Throwable throwme = null;
            try {
                // replay mocks so that they behave appropriately during test execution
                if (mocks != null && !mocks.isEmpty()) {
                    documenter.log("** Replaying Mocks [{0}]", mocks);
                    EasyMock.replay(mocks.toArray());
                }

                if (autowiredMocks != null && !autowiredMocks.isEmpty()) {
                    documenter.log("** Replaying Autowired Mocks [{0}]", autowiredMocks);
                    for (Field autowiredField : autowiredMocks) {
                        Object autowiredMock = ReflectionUtils.getField(autowiredField, testInstance);
                        EasyMock.replay(autowiredMock);
                    }
                }

                if (singletons != null && !singletons.isEmpty()) {
                    documenter.log("** Setting Singletons [{0}]", singletons);
                    for (Map.Entry<Class<?>, List<?>> entries : singletons.entrySet()) {
                        Field field = (Field) entries.getValue().get(0);
                        Object mockedInstance = entries.getValue().get(2);
                        documenter.log("** Setting {0} to {1}", field, mockedInstance);
                        field.set(null, mockedInstance);
                        documenter.log("*** Singleton Instance {0}", field.get(null));
                    }
                }
                documenter.log("** Executing Test...");
                base.evaluate();
                documenter.log("** Finish Test execution");
            }
            catch (AssertionError t) {
                documenter.logError(t, "+++ An Assertion failed");
                throwme = t;
                throw t;
            }
            catch (Throwable t) {
                documenter.logError(t, "+++ An Exception was Thrown : {0}", t.getClass());
                throwme = t;
                throw t;
            } finally {
                // verify the mocks were executed.
                // (this is more like white box testing though as you need to know exactly how a method behaves)
                if (throwme == null) {
                    // don't verify the mocks on an exception
                    if (mocks != null && !mocks.isEmpty()) {
                        documenter.log("** Verifying Mocks [{0}]", mocks);
                        EasyMock.verify(mocks.toArray());
                    }

                    if (autowiredMocks != null && !autowiredMocks.isEmpty()) {
                        documenter.log("** Verifying Autowired Mocks [{0}]", autowiredMocks);
                        for (Field autowiredField : autowiredMocks) {
                            Object autowiredMock = ReflectionUtils.getField(autowiredField, testInstance);
                            EasyMock.verify(autowiredMock);
                        }
                    }
                }

                if (mocks != null && !mocks.isEmpty()) {
                    documenter.log("** Resetting Mocks");
                    EasyMock.reset(mocks.toArray());
                }

                if (autowiredMocks != null && !autowiredMocks.isEmpty()) {
                    documenter.log("** Resetting Autowired Mocks [{0}]", autowiredMocks);
                    for (Field autowiredField : autowiredMocks) {
                        Object autowiredMock = ReflectionUtils.getField(autowiredField, testInstance);
                        EasyMock.reset(autowiredMock);
                    }
                }

                if (singletons != null && !singletons.isEmpty()) {
                    documenter.log("** Resetting Singletons [{0}]", singletons);
                    for (Map.Entry<Class<?>, List<?>> entries : singletons.entrySet()) {
                        Field field = (Field) entries.getValue().get(0);
                        Object concreteInstance = entries.getValue().get(1);
                        documenter.log("** Setting {0} to {1}", field, concreteInstance);
                        field.set(null, concreteInstance);
                        documenter.log("*** Singleton Instance {0}", field.get(null));
                    }
                }
            }

            if (documenter == null) {
                Logger.info(log, "@@@ Ending Test {0}", base);
            } else {
                documenter.reportEndTest();
            }
        }
    }
}
