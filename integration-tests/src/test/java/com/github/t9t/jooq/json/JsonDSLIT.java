package com.github.t9t.jooq.json;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static com.github.t9t.jooq.generated.Tables.JSON_TEST;
import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class JsonDSLIT {
    private static final DSLContext dsl = DSL.using(TestDb.createDataSource(), SQLDialect.POSTGRES_10);
    private static final String testRowName = "json-dsl";

    private final String expected;
    private final Field<String> fieldToSelect;

    public JsonDSLIT(String name, String expected, Field<String> fieldToSelect) {
        this.expected = requireNonNull(expected, "expected");
        this.fieldToSelect = requireNonNull(fieldToSelect, "fieldToSelect");
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> params() {
        return Arrays.asList(
                params("fieldByKey_json_select", "\"Hello, JSON world!\"", JsonDSL.fieldByKey(JSON_TEST.DATA, "str")),
                params("fieldByKey_json_select_twoLevels", "5521", JsonDSL.fieldByKey(JsonDSL.fieldByKey(JSON_TEST.DATA, "obj"), "i")),
                params("fieldByKey_jsonb_select", "\"Hello, JSONB world!\"", JsonDSL.fieldByKey(JSON_TEST.DATAB, "str")),
                params("fieldByKey_jsonb_select_twoLevels", "5521", JsonDSL.fieldByKey(JsonDSL.fieldByKey(JSON_TEST.DATAB, "obj"), "i"))
        );
    }

    private static Object[] params(String name, String expected, Field<String> field) {
        return new Object[]{name, expected, field};
    }

    @Before
    public void setUp() {
        dsl.deleteFrom(JSON_TEST).execute();

        String template = "{\"obj\": {\"i\": 5521, \"b\": true}, \"arr\": [{\"d\": 4408}, 10, true, \"s\"], \"num\": 1337, \"str\": \"Hello, %s world!\", \"n\": null}";
        assertEquals(1, dsl.insertInto(JSON_TEST)
                .set(JSON_TEST.NAME, testRowName)
                .set(JSON_TEST.DATA, String.format(template, "JSON"))
                .set(JSON_TEST.DATAB, String.format(template, "JSONB"))
                .execute());
        assertEquals(1, dsl.fetchCount(JSON_TEST));
    }

    @Test
    public void test() {
        assertEquals(expected, select(fieldToSelect));
    }

    private static String select(Field<String> field) {
        return dsl.select(field)
                .from(JSON_TEST)
                .where(JSON_TEST.NAME.eq(testRowName))
                .fetchOne().value1();
    }
}
