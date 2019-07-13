package com.github.t9t.jooq.jsonb;


import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;

import static com.github.t9t.jooq.generated.Tables.JSON_TEST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonbStringBindingIT {
    private DSLContext dsl;

    @Before
    public void setUp() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setURL("jdbc:postgresql://localhost:23719/jooq");
        ds.setUser("jooq");
        ds.setPassword("jooq");

        this.dsl = DSL.using(ds, SQLDialect.POSTGRES_10);

        // Verify connection naively
        assertEquals(Integer.valueOf(5521), dsl.select(DSL.value("5521")).fetchOneInto(Integer.class));
    }

    @Test
    public void selectJson() {
        Record1<String> r = dsl.select(JSON_TEST.DATA)
                .from(JSON_TEST)
                .where(JSON_TEST.NAME.eq("both"))
                .fetchOne();

        assertEquals("{\"json\": {\"int\": 100, \"str\": \"Hello, JSON world!\", \"object\": {\"v\":  200}, \"n\": null}}", r.value1());
    }

    @Test
    public void selectJsonEmpty() {
        Record1<String> r = dsl.select(JSON_TEST.DATA)
                .from(JSON_TEST)
                .where(JSON_TEST.NAME.eq("empty"))
                .fetchOne();

        assertEquals("{}", r.value1());
    }

    @Test
    public void selectJsonNull() {
        Record1<String> r = dsl.select(JSON_TEST.DATA)
                .from(JSON_TEST)
                .where(JSON_TEST.NAME.eq("null"))
                .fetchOne();

        assertNull(r.value1());
    }

    @Test
    public void selectJsonb() {
        Record1<String> r = dsl.select(JSON_TEST.DATAB)
                .from(JSON_TEST)
                .where(JSON_TEST.NAME.eq("both"))
                .fetchOne();

        assertEquals("{\"jsonb\": {\"n\": null, \"int\": 100, \"str\": \"Hello, JSON world!\", \"object\": {\"v\": 200}}}", r.value1());
    }

    @Test
    public void selectJsonbEmpty() {
        Record1<String> r = dsl.select(JSON_TEST.DATAB)
                .from(JSON_TEST)
                .where(JSON_TEST.NAME.eq("empty"))
                .fetchOne();

        assertEquals("{}", r.value1());
    }

    @Test
    public void selectJsonbNull() {
        Record1<String> r = dsl.select(JSON_TEST.DATAB)
                .from(JSON_TEST)
                .where(JSON_TEST.NAME.eq("null"))
                .fetchOne();

        assertNull(r.value1());
    }

    @Test
    public void selectBothJsonAndJsonB() {
        Record2<String, String> r = dsl.select(JSON_TEST.DATA, JSON_TEST.DATAB)
                .from(JSON_TEST)
                .where(JSON_TEST.NAME.eq("null"))
                .fetchOne();

        assertNull(r.value1());
        assertNull(r.value2());
    }

}