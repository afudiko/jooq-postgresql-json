package com.github.t9t.jooq.json;

import org.jooq.impl.DSL;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static com.github.t9t.jooq.json.JsonbDSL.contains;
import static com.github.t9t.jooq.json.JsonbDSL.field;

public class JsonbDSLContainsIT extends AbstractJsonDSLTest {
    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> params() {
        return generateParams("contains", Arrays.asList(
                btest("simple").selecting(DSL.field(contains(jsonb, field("{\"num\": 1337}")))).expect(true),
                btest("complex").selecting(DSL.field(contains(jsonb, field("{\"obj\": {\"i\": 5521, \"b\": true}}")))).expect(true),
                btest("arrayInt").forArray().selecting(DSL.field(contains(jsonb, field("10")))).expect(true),
                btest("arrayStr").forArray().selecting(DSL.field(contains(jsonb, field("\"jsonb array\"")))).expect(true),
                btest("arrayArray").forArray().selecting(DSL.field(contains(jsonb, field("[\"jsonb array\", 10]")))).expect(true),
                btest("doesn't").selecting(DSL.field(contains(jsonb, field("{\"num\": 1338}")))).expect(false)
        ));
    }
}
