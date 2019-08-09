package com.github.t9t.jooq.json;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static com.github.t9t.jooq.generated.Tables.JSON_TEST;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public abstract class AbstractJsonDSLTest {
    private static final DSLContext dsl = DSL.using(TestDb.createDataSource(), SQLDialect.POSTGRES_10);

    static final String genericRow = "json-dsl";
    static final String arrayRow = "array";

    @Parameterized.Parameter
    public String testName;
    @Parameterized.Parameter(1)
    public String dataType;
    @Parameterized.Parameter(2)
    public String rowName;
    @Parameterized.Parameter(3)
    public String expected;
    @Parameterized.Parameter(4)
    public Field<String> fieldToSelect;

    static List<Object[]> generateParams(BiFunction<String, Field<String>, List<Object[]>> testParamFunc) {
        List<Object[]> params = new ArrayList<>();
        for (String type : Arrays.asList("json", "jsonb")) {
            Field<String> f = "json".equals(type) ? JSON_TEST.DATA : JSON_TEST.DATAB;
            params.addAll(testParamFunc.apply(type, f));
        }
        return params;
    }

    static Object[] params(String name, String type, String expected, Field<String> field) {
        return params(name, type, genericRow, expected, field);
    }

    static Object[] params(String name, String type, String rowName, String expected, Field<String> field) {
        return new Object[]{name, type, rowName, expected, field};
    }

    @Before
    public void setUp() {
        dsl.deleteFrom(JSON_TEST).execute();

        String template = "{\"obj\": {\"i\": 5521, \"b\": true}, \"arr\": [{\"d\": 4408}, 10, true, \"s\"], \"num\": 1337, \"str\": \"Hello, %s world!\", \"n\": null}";
        String artmpl = "[{\"d\": 4408}, 10, true, \"%s array\"]";
        assertEquals(2, dsl.insertInto(JSON_TEST)
                .columns(JSON_TEST.NAME, JSON_TEST.DATA, JSON_TEST.DATAB)
                .values(genericRow, String.format(template, "json"), String.format(template, "jsonb"))
                .values(arrayRow, String.format(artmpl, "json"), String.format(artmpl, "jsonb"))
                .execute());
        assertEquals(2, dsl.fetchCount(JSON_TEST));
    }

    @Test
    public void test() {
        assertEquals(expected, select(rowName, fieldToSelect));
    }

    private static String select(String rowName, Field<String> field) {
        return dsl.select(field)
                .from(JSON_TEST)
                .where(JSON_TEST.NAME.eq(rowName))
                .fetchOne().value1();
    }
}
