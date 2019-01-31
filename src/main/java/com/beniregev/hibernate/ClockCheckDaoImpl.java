package com.beniregev.hibernate;

import com.beniregev.model.Machine;
import com.beniregev.util.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class ClockCheckDaoImpl extends HibernateDaoSupport implements ClockCheckDAO {

    private static final Log log = LogFactory.getLog(ClockCheckDaoImpl.class);

    private JdbcTemplate jdbcTemplate;

    private int allowedOffsetSeconds;
    private int allowedOffsetLogSeconds;
    private TimeSource timeSource = DEFAULT_TIMESOURCE;


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean doClockCheck() {

        // Query the database for its current time in GMT. We MUST use GMT here because of a bug that causes
        // the database's reported local time to be off by an hour close to the daylight savings time changeover.
        String sql = "select sys_extract_utc(current_timestamp) from dual";
        List<Timestamp> queryResult = jdbcTemplate.query(sql, new RowMapper() {
            public Object mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                return rs.getTimestamp(1, Calendar.getInstance(TimeZone.getTimeZone("GMT")));
            }
        });
        if (queryResult.size() != 1) {
            throw new IllegalStateException("Failed to query database for its current time.");
        }

        // Compare database time to JVM's.
        Timestamp ts = queryResult.get(0);
        long dbTime = ts.getTime();
        long ourTime = timeSource.getTime();
        long diff =	ourTime - dbTime;

        if (log.isTraceEnabled()) {
            Logger.trace(log, "Server is {0}ms off from the OLTP database.", diff);
        }

        long inSeconds = Math.abs(diff) / 1000;

        if (inSeconds > allowedOffsetLogSeconds) {
            Logger.error(log, "Warning...Server is {0}ms off from the OLTP database.", diff);
        }

        return inSeconds < allowedOffsetSeconds;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Set<Machine> getWackedMachines() {
        return new HashSet<Machine>(getHibernateTemplate().loadAll(Machine.class));
    }

    /**
     * {@inheritDoc}
     */
    public void removeMachine(final Machine machine) {
        getHibernateTemplate().delete(machine);
    }

    /**
     * {@inheritDoc}
     */
    public boolean addMachine(final String host) {
        Machine m = (Machine)getHibernateTemplate().get(Machine.class, host);
        if (m == null) {
            getHibernateTemplate().saveOrUpdate(new Machine(host));
            return true;
        }
        return false;
    }

    /**
     * The allowed differential between an application server clock and a database clock before
     * the system will be put on lock down. Specified in seconds.
     * @param allowedOffsetSeconds the allowed offset in seconds.
     */
    @Required
    public void setAllowedOffsetSeconds(final int allowedOffsetSeconds) {
        this.allowedOffsetSeconds = allowedOffsetSeconds;
    }


    /**
     * The allowed differential between an application server clock and a database clock before
     * the system will log an error. Specified in seconds.
     * @param allowedOffsetLogSeconds the allowed offset in seconds.
     */
    @Required
    public void setAllowedOffsetLogSeconds(final int allowedOffsetLogSeconds) {
        this.allowedOffsetLogSeconds = allowedOffsetLogSeconds;
    }

    /**
     * Used to run the SQL query to determine the database time.
     * @param jdbcTemplate the jdbcTemplate to set
     */
    @Required
    public void setJdbcTemplate(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Set the object that will give the current time that will be compared against the DB clock.
     * @param timeSource source
     */
    public void setTimeSource(final TimeSource timeSource) {
        this.timeSource = timeSource;
    }

    /**
     * for unit testing.
     */
    public interface TimeSource {
        /**
         * @return the current time in milliseconds.
         */
        long getTime();
    }

    public static final TimeSource DEFAULT_TIMESOURCE = new TimeSource() {
        public long getTime() {
            return System.currentTimeMillis();
        }
    };
}
