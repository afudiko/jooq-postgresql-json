package com.github.t9t.jooq.json;

import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.impl.DSL;

import java.util.Collection;

/**
 * <p>Functions for {@code json} PostgreSQL operator support in jOOQ</p>
 *
 * <p>Reference: <a href="https://www.postgresql.org/docs/11/functions-json.html">https://www.postgresql.org/docs/11/functions-json.html</a></p>
 */
public final class JsonDSL {
    /**
     * Create a jOOQ {@link Field} wrapping a {@link JSON} object representing a {@code json} value for the JSON
     * string. <b>Note</b> that the JSON is <i>not</i> validated (any formatting errors will only occur when
     * interacting with the database).
     *
     * @param json JSON string
     * @return {@code json} {@code Field} for the JSON string
     */
    public static Field<JSON> field(String json) {
        return field(JSON.valueOf(json));
    }

    /**
     * Create a jOOQ {@link Field} wrapping the {@link JSON} object.
     *
     * @param json {@code JSON} object to wrap
     * @return {@code json} {@code Field} for the {@code JSON} object
     */
    public static Field<JSON> field(JSON json) {
        return DSL.field("{0}", JSON.class, json);
    }

    /**
     * <p>Get JSON array element (indexed from zero, negative integers count from the end), using the
     * <code>-&gt;</code> operator</p>
     *
     * <p>Example: <code>'[{"a":"foo"},{"b":"bar"},{"c":"baz"}]'::json-&gt;2</code></p>
     * <p>Example result: <code>{"c":"baz"}</code></p>
     *
     * @param jsonField A JSON {@code Field} containing an array to get the array element from
     * @param index     Array index; negative values count from the end
     * @return A {@code Field} representing the extracted array element
     */
    public static Field<JSON> arrayElement(Field<JSON> jsonField, int index) {
        return DSL.field("{0}->{1}", JSON.class, jsonField, index);
    }

    /**
     * <p>Get JSON array element as {@code text} rather than {@code json(b)} (indexed from zero, negative integers
     * count from the end), using the <code>-&gt;&gt;</code> operator</p>
     *
     * <p>Example: <code>'[1,2,3]'::json-&gt;&gt;2</code></p>
     * <p>Example result: <code>3</code></p>
     *
     * @param jsonField A JSON {@code Field} containing an array to get the array element from
     * @param index     Array index; negative values count from the end
     * @return A {@code Field} representing the extracted array element, as text
     */
    public static Field<String> arrayElementText(Field<JSON> jsonField, int index) {
        return DSL.field("{0}->>{1}", String.class, jsonField, index);
    }

    /**
     * <p>Get JSON object field by key using the <code>-&gt;</code> operator</p>
     *
     * <p>Example: <code>'{"a": {"b":"foo"}}'::json-&gt;'a'</code></p>
     * <p>Example result: <code>{"b":"foo"}</code></p>
     *
     * @param jsonField The JSON {@code Field} to extract the field from
     * @param key       JSON field key name
     * @return A {@code Field} representing the extracted value
     */
    public static Field<JSON> fieldByKey(Field<JSON> jsonField, String key) {
        return DSL.field("{0}->{1}", JSON.class, jsonField, key);
    }

    /**
     * <p>Get JSON object field as {@code text} rather than {@code json(b)}, using the <code>-&gt;&gt;</code>
     * operator</p>
     *
     * <p>Example: <code>'{"a":1,"b":2}'::json-&gt;&gt;'b'</code></p>
     * <p>Example result: <code>2</code></p>
     *
     * @param jsonField The JSON {@code Field} to extract the field from
     * @param key       JSON field key name
     * @return A {@code Field} representing the extracted array element, as text
     */
    public static Field<String> fieldByKeyText(Field<JSON> jsonField, String key) {
        return DSL.field("{0}->>{1}", String.class, jsonField, key);
    }

    /**
     * <p>Get JSON object at specified path using the <code>#&gt;</code> operator</p>
     *
     * <p>Example: <code>'{"a": {"b":{"c": "foo"}}}'::json#&gt;'{a,b}'</code></p>
     * <p>Example result: <code>{"c": "foo"}</code></p>
     *
     * @param jsonField The JSON {@code Field} to extract the path from
     * @param path      Path to the the object to return
     * @return A {@code Field} representing the object at the specified path
     * @see #objectAtPath(Field, Collection)
     */
    public static Field<JSON> objectAtPath(Field<JSON> jsonField, String... path) {
        return DSL.field("{0}#>{1}", JSON.class, jsonField, DSL.array(path));
    }

    /**
     * <p>Get JSON object at specified path using the <code>#&gt;</code> operator</p>
     *
     * <p>Example: <code>'{"a": {"b":{"c": "foo"}}}'::json#&gt;'{a,b}'</code></p>
     * <p>Example result: <code>{"c": "foo"}</code></p>
     *
     * @param jsonField The JSON {@code Field} to extract the path from
     * @param path      Path to the the object to return
     * @return A {@code Field} representing the object at the specified path
     * @see #objectAtPath(Field, String...)
     */
    public static Field<JSON> objectAtPath(Field<JSON> jsonField, Collection<String> path) {
        return objectAtPath(jsonField, path.toArray(new String[0]));
    }

    /**
     * <p>Get JSON object at specified path as {@code text} rather than {@code json(b)}, using the <code>#&gt;&gt;</code>
     * operator</p>
     *
     * <p>Example: <code>'{"a":[1,2,3],"b":[4,5,6]}'::json#&gt;&gt;'{a,2}'</code></p>
     * <p>Example result: <code>3</code></p>
     *
     * @param jsonField The JSON {@code Field} to extract the path from
     * @param path      Path to the the object to return
     * @return A {@code Field} representing the object at the specified path, as text
     * @see #objectAtPathText(Field, Collection)
     */
    public static Field<String> objectAtPathText(Field<JSON> jsonField, String... path) {
        return DSL.field("{0}#>>{1}", String.class, jsonField, DSL.array(path));
    }

    /**
     * <p>Get JSON object at specified path as {@code text} rather than {@code json(b)}, using the <code>#&gt;&gt;</code>
     * operator</p>
     *
     * <p>Example: <code>'{"a":[1,2,3],"b":[4,5,6]}'::json#&gt;&gt;'{a,2}'</code></p>
     * <p>Example result: <code>3</code></p>
     *
     * @param jsonField The JSON {@code Field} to extract the path from
     * @param path      Path to the the object to return
     * @return A {@code Field} representing the object at the specified path, as text
     * @see #objectAtPath(Field, String...)
     */
    public static Field<String> objectAtPathText(Field<JSON> jsonField, Collection<String> path) {
        return objectAtPathText(jsonField, path.toArray(new String[0]));
    }

    /**
     * <p>Returns the number of elements in the outermost JSON array.</p>
     *
     * <p>Example: <code>json_array_length('[1,2,3,{"f1":1,"f2":[5,6]},4]')</code></p>
     * <p>Example result: <code>5</code></p>
     *
     * @param jsonField The JSON {@code Field} containing an array to measure the length of
     * @return Length of the array
     */
    public static Field<Integer> arrayLength(Field<Json> jsonField) {
        return DSL.field("json_array_length({0})", Integer.class, jsonField);
    }

    /**
     * <p>Returns JSON value pointed to by {@code path} (equivalent to <code>#&gt;</code> operator, ie.
     * {@link #objectAtPath(Field, String...)}).</p>
     *
     * <p>Example: <code>json_extract_path('{"f2":{"f3":1},"f4":{"f5":99,"f6":"foo"}}','f4')</code></p>
     * <p>Example result: <code>{"f5":99,"f6":"foo"}</code></p>
     *
     * @param jsonField The JSON {@code Field} to extract the path from
     * @param path      Path to the the object to return
     * @return A {@code Field} representing the object at the specified path
     * @see #objectAtPath(Field, String...)
     * @see #objectAtPath(Field, Collection)
     * @see #extractPath(Field, Collection)
     */
    public static Field<Json> extractPath(Field<Json> jsonField, String... path) {
        return DSL.field("json_extract_path({0}, VARIADIC {1})", Json.class, jsonField, DSL.array(path));
    }

    /**
     * <p>Returns JSON value pointed to by {@code path} (equivalent to <code>#&gt;</code> operator, ie.
     * {@link #objectAtPath(Field, Collection)}).</p>
     *
     * <p>Example: <code>json_extract_path('{"f2":{"f3":1},"f4":{"f5":99,"f6":"foo"}}','f4')</code></p>
     * <p>Example result: <code>{"f5":99,"f6":"foo"}</code></p>
     *
     * @param jsonField The JSON {@code Field} to extract the path from
     * @param path      Path to the the object to return
     * @return A {@code Field} representing the object at the specified path
     * @see #objectAtPath(Field, String...)
     * @see #objectAtPath(Field, Collection)
     * @see #extractPath(Field, String...)
     */
    public static Field<Json> extractPath(Field<Json> jsonField, Collection<String> path) {
        return extractPath(jsonField, path.toArray(new String[0]));
    }

    /**
     * <p>Returns JSON value pointed to by {@code path} as text (equivalent to <code>#&gt;&gt;</code> operator, ie.
     * {@link #objectAtPathText(Field, String...)}).</p>
     *
     * <p>Example: <code>json_extract_path_text('{"f2":{"f3":1},"f4":{"f5":99,"f6":"foo"}}','f4', 'f6')</code></p>
     * <p>Example result: <code>foo</code></p>
     *
     * @param jsonField The JSON {@code Field} to extract the path from
     * @param path      Path to the the object to return
     * @return A {@code Field} representing the object at the specified path, as text
     * @see #objectAtPathText(Field, String...)
     * @see #objectAtPathText(Field, Collection)
     * @see #extractPathText(Field, Collection)
     */
    public static Field<String> extractPathText(Field<Json> jsonField, String... path) {
        return DSL.field("json_extract_path_text({0}, VARIADIC {1})", String.class, jsonField, DSL.array(path));
    }

    /**
     * <p>Returns JSON value pointed to by {@code path} as text (equivalent to <code>#&gt;&gt;</code> operator, ie.
     * {@link #objectAtPathText(Field, Collection)}).</p>
     *
     * <p>Example: <code>json_extract_path_text('{"f2":{"f3":1},"f4":{"f5":99,"f6":"foo"}}','f4', 'f6')</code></p>
     * <p>Example result: <code>foo</code></p>
     *
     * @param jsonField The JSON {@code Field} to extract the path from
     * @param path      Path to the the object to return
     * @return A {@code Field} representing the object at the specified path, as text
     * @see #objectAtPathText(Field, String...)
     * @see #objectAtPathText(Field, Collection)
     * @see #extractPathText(Field, String...)
     */
    public static Field<String> extractPathText(Field<Json> jsonField, Collection<String> path) {
        return extractPathText(jsonField, path.toArray(new String[0]));
    }

    /**
     * <p>Returns the type of the outermost JSON value as a text string. Possible types are {@code object}, {@code array},
     * {@code string}, {@code number}, {@code boolean}, and {@code null}.</p>
     *
     * <p>Example: <code>json_typeof('-123.4')</code></p>
     * <p>Example result: <code>number</code></p>
     *
     * @param jsonField The JSON {@code Field} to determine the type of
     * @return The JSON type
     */
    public static Field<String> typeOf(Field<Json> jsonField) {
        return DSL.field("json_typeof({0})", String.class, jsonField);
    }

    /**
     * <p>Returns a JSON {@code Field} with all object fields that have {@code null} values omitted. Other {@code null}
     * values (eg. in arrays) are untouched.</p>
     *
     * <p>Example: <code>json_strip_nulls('[{"f1":1,"f2":null},2,null,3]')</code></p>
     * <p>Example result: <code>[{"f1":1},2,null,3]</code></p>
     *
     * @param jsonField The JSON {@code Field} to remove {@code null} values from
     * @return A JSON {@code Field} with {@code null} object fields removed
     */
    public static Field<Json> stripNulls(Field<Json> jsonField) {
        return DSL.field("json_strip_nulls({0})", Json.class, jsonField);
    }
}
