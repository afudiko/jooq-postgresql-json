package com.github.t9t.jooq.json;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.impl.DSL;

import java.util.Collection;

/**
 * <p>Functions for {@code jsonb} PostgreSQL operator support in jOOQ</p>
 *
 * <p>Reference: <a href="https://www.postgresql.org/docs/11/functions-json.html">https://www.postgresql.org/docs/11/functions-json.html</a></p>
 */
public final class JsonbDSL {
    /**
     * Create a jOOQ {@link Field} wrapping a {@link JSONB} object representing a {@code jsonb} value for the JSON
     * string. <b>Note</b> that the JSON is <i>not</i> validated (any formatting errors will only occur when
     * interacting with the database).
     *
     * @param json JSON string
     * @return {@code jsonb} {@code Field} for the JSON string
     */
    public static Field<JSONB> field(String json) {
        return field(JSONB.valueOf(json));
    }

    /**
     * Create a jOOQ {@link Field} wrapping the {@link JSONB} object.
     *
     * @param jsonb {@code JSONB} object to wrap
     * @return {@code jsonb} {@code Field} for the {@code JSONB} object
     */
    public static Field<JSONB> field(JSONB jsonb) {
        return DSL.field("{0}", JSONB.class, jsonb);
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
    public static Field<JSONB> arrayElement(Field<JSONB> jsonField, int index) {
        return DSL.field("{0}->{1}", JSONB.class, jsonField, index);
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
    public static Field<String> arrayElementText(Field<JSONB> jsonField, int index) {
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
    public static Field<JSONB> fieldByKey(Field<JSONB> jsonField, String key) {
        return DSL.field("{0}->{1}", JSONB.class, jsonField, key);
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
    public static Field<String> fieldByKeyText(Field<JSONB> jsonField, String key) {
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
    public static Field<JSONB> objectAtPath(Field<JSONB> jsonField, String... path) {
        return DSL.field("{0}#>{1}", JSONB.class, jsonField, DSL.array(path));
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
    public static Field<JSONB> objectAtPath(Field<JSONB> jsonField, Collection<String> path) {
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
    public static Field<String> objectAtPathText(Field<JSONB> jsonField, String... path) {
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
    public static Field<String> objectAtPathText(Field<JSONB> jsonField, Collection<String> path) {
        return objectAtPathText(jsonField, path.toArray(new String[0]));
    }

    /**
     * <p>Does the {@code left} JSON value contain the {@code right} JSON path/value entries at the top level? Uses the
     * {@code @>} operator.</p>
     *
     * <p>Example: <code>'{"a":1, "b":2}'::jsonb @&gt; '{"b":2}'::jsonb</code></p>
     *
     * @param left  The JSON {@code Field} that should contain {@code right}
     * @param right The JSON {@code Field} that should be contained in {@code left}
     * @return A {@code Condition} representing whether {@code left} is contained in {@code right}
     */
    public static Condition contains(Field<JSONB> left, Field<JSONB> right) {
        return DSL.condition("{0} @> {1}", left, right);
    }

    /**
     * <p>Are the {@code left} JSON path/value entries contained at the top level within the {@code right} JSON value?
     * Uses the {@code <@} operator.</p>
     *
     * <p>Example: <code>'{"b":2}'::jsonb &lt;@ '{"a":1, "b":2}'::jsonb</code></p>
     *
     * @param left  The JSON {@code Field} that should be contained in {@code right}
     * @param right The JSON {@code Field} that should contain {@code left}
     * @return A {@code Condition} representing whether {@code right} is contained in {@code left}
     */
    public static Condition containedIn(Field<JSONB> left, Field<JSONB> right) {
        return DSL.condition("{0} <@ {1}", left, right);
    }

    /**
     * <p>Does the <i>string</i> exist as a top-level key within the JSON value? Uses the {@code ?} operator.</p>
     *
     * <p>Example: <code>'{"a":1, "b":2}'::jsonb ? 'b'</code></p>
     *
     * @param f   The JSON {@code Field} that should contain the {@code key}
     * @param key The key that should exist at the top level in the JSON
     * @return A {@code Condition} representing whether the key is contained in the JSON value
     */
    public static Condition hasKey(Field<JSONB> f, String key) {
        return DSL.condition("{0} ?? {1}", f, key);
    }

    /**
     * <p>Do any of these array <i>strings</i> exist as top-level keys? Uses the {@code ?|} operator.</p>
     *
     * <p>Example: <code>'{"a":1, "b":2, "c":3}'::jsonb ?| array['b', 'c']</code></p>
     *
     * @param f    The JSON {@code Field} that should contain any of the {@code keys}
     * @param keys List of keys that may exist in the JSON value
     * @return A {@code Condition} representing whether any of the {@code keys} exist
     * @see #hasAnyKey(Field, Collection)
     */
    public static Condition hasAnyKey(Field<JSONB> f, String... keys) {
        return DSL.condition("{0} ??| {1}", f, DSL.array(keys));
    }

    /**
     * <p>Do any of these array <i>strings</i> exist as top-level keys? Uses the {@code ?|} operator.</p>
     *
     * <p>Example: <code>'{"a":1, "b":2, "c":3}'::jsonb ?| array['b', 'c']</code></p>
     *
     * @param f    The JSON {@code Field} that should contain any of the {@code keys}
     * @param keys List of keys that may exist in the JSON value
     * @return A {@code Condition} representing whether any of the {@code keys} exist
     * @see #hasAnyKey(Field, String...)
     */
    public static Condition hasAnyKey(Field<JSONB> f, Collection<String> keys) {
        return hasAnyKey(f, keys.toArray(new String[0]));
    }

    /**
     * <p>Do all of these array <i>strings</i> exist as top-level keys? Uses the {@code ?&} operator.</p>
     *
     * <p>Example: <code>'["a", "b"]'::jsonb ?&amp; array['a', 'b']</code></p>
     *
     * @param f    The JSON {@code Field} that should contain all of the {@code keys}
     * @param keys List of keys that all should exist in the JSON value
     * @return A {@code Condition} representing whether all of the {@code keys} exist
     * @see #hasAllKeys(Field, Collection)
     */
    public static Condition hasAllKeys(Field<JSONB> f, String... keys) {
        return DSL.condition("{0} ??& {1}", f, keys);
    }

    /**
     * <p>Do all of these array <i>strings</i> exist as top-level keys? Uses the {@code ?&} operator.</p>
     *
     * <p>Example: <code>'["a", "b"]'::jsonb ?&amp; array['a', 'b']</code></p>
     *
     * @param f    The JSON {@code Field} that should contain all of the {@code keys}
     * @param keys List of keys that all should exist in the JSON value
     * @return A {@code Condition} representing whether all of the {@code keys} exist
     * @see #hasAllKeys(Field, String...)
     */
    public static Condition hasAllKeys(Field<JSONB> f, Collection<String> keys) {
        return hasAllKeys(f, keys.toArray(new String[0]));
    }

    /**
     * <p>Concatenate two {@code jsonb} values into a new {@code jsonb} value using the {@code ||} operator.</p>
     *
     * <p>Example: <code>'["a", "b"]'::jsonb || '["c", "d"]'::jsonb</code></p>
     * <p>Example result: <code>["a", "b", "c", "d"]</code></p>
     *
     * @param field1 Field to concatenate {@code field2} to
     * @param field2 Field to concatenate to {@code field1}
     * @return A {@code Field} representing a concatenation of the two JSON fields
     */
    public static Field<JSONB> concat(Field<JSONB> field1, Field<JSONB> field2) {
        return DSL.field("{0} || {1}", JSONB.class, field1, field2);
    }

    /**
     * <p>Delete key/value pair or <i>string</i> element from left operand. Key/value pairs are matched based on their
     * key value. Uses the {@code -} operator.</p>
     *
     * <p>Example: <code>'{"a": "b", "c": "d"}'::jsonb - 'a'</code></p>
     * <p>Example result: <code>{"c": "d"}</code></p>
     *
     * @param f            JSON {@code Field} to delete the key or element from
     * @param keyOrElement The key name or element value to delete from the JSON field
     * @return A {@code Field} representing the original field with the key or element deleted
     */
    public static Field<JSONB> delete(Field<JSONB> f, String keyOrElement) {
        return DSL.field("{0} - {1}", JSONB.class, f, keyOrElement);
    }

    /**
     * <p>Delete multiple key/value pairs or <i>string</i> elements from left operand. Key/value pairs are matched
     * based on their key value. Uses the {@code -} operator.</p>
     *
     * <p>Example: <code>'{"a": "b", "c": "d", "e": "f"}'::jsonb - '{a,c}'::text[]</code></p>
     * <p>Example result: <code>{"e", "f"}</code></p>
     *
     * @param f              JSON {@code Field} to delete the keys or elements from
     * @param keysOrElements The key names or element values to delete from the JSON field
     * @return A {@code Field} representing the original field with the keys or elements deleted
     */
    public static Field<JSONB> delete(Field<JSONB> f, String... keysOrElements) {
        return DSL.field("{0} - {1}", JSONB.class, f, DSL.array(keysOrElements));
    }

    /**
     * <p>Delete the array element with specified index (Negative integers count from the end). Throws an error if top
     * level container is not an array. Uses the {@code -} operator.</p>
     *
     * <p>Example: <code>'["a", "b"]'::jsonb - 1</code></p>
     * <p>Example result: <code>["a"]</code></p>
     *
     * @param f JSON {@code Field} containing an array to delete the element from
     * @param index Array index to delete; negative values count from the end of the array
     * @return A {@code Field} representing the field with the array element removed
     */
    public static Field<JSONB> deleteElement(Field<JSONB> f, int index) {
        return DSL.field("{0} - {1}", JSONB.class, f, index);
    }

    /**
     * <p>Delete the field or element with specified path (for JSON arrays, negative integers count from the end). Uses
     * the {@code #-} operator.</p>
     *
     * <p>Example: <code>'["a", {"b":1,"c":2}]'::jsonb #- '{1,b}'</code></p>
     * <p>Example result: <code>["a", {"c": 2}]</code></p>
     *
     * @param f JSON {@code Field} to delete the selected path from
     * @param path Path to the JSON element to remove
     * @return A {@code Field} representing the field with the chosen path removed
     */
    public static Field<JSONB> deletePath(Field<JSONB> f, String... path) {
        return DSL.field("{0} #- {1}", JSONB.class, f, DSL.array(path));
    }


    /**
     * <p>Returns the number of elements in the outermost JSON array.</p>
     *
     * <p>Example: <code>jsonb_array_length('[1,2,3,{"f1":1,"f2":[5,6]},4]')</code></p>
     * <p>Example result: <code>5</code></p>
     *
     * @param jsonField The JSON {@code Field} containing an array to measure the length of
     * @return Length of the array
     */
    public static Field<Integer> arrayLength(Field<Jsonb> jsonField) {
        return DSL.field("jsonb_array_length({0})", Integer.class, jsonField);
    }

    /**
     * <p>Returns JSON value pointed to by {@code path} (equivalent to <code>#&gt;</code> operator, ie.
     * {@link #objectAtPath(Field, String...)}).</p>
     *
     * <p>Example: <code>jsonb_extract_path('{"f2":{"f3":1},"f4":{"f5":99,"f6":"foo"}}','f4')</code></p>
     * <p>Example result: <code>{"f5":99,"f6":"foo"}</code></p>
     *
     * @param jsonField The JSON {@code Field} to extract the path from
     * @param path      Path to the the object to return
     * @return A {@code Field} representing the object at the specified path
     * @see #objectAtPath(Field, String...)
     * @see #objectAtPath(Field, Collection)
     * @see #extractPath(Field, Collection)
     */
    public static Field<Jsonb> extractPath(Field<Jsonb> jsonField, String... path) {
        return DSL.field("jsonb_extract_path({0}, VARIADIC {1})", Jsonb.class, jsonField, DSL.array(path));
    }

    /**
     * <p>Returns JSON value pointed to by {@code path} (equivalent to <code>#&gt;</code> operator, ie.
     * {@link #objectAtPath(Field, Collection)}).</p>
     *
     * <p>Example: <code>jsonb_extract_path('{"f2":{"f3":1},"f4":{"f5":99,"f6":"foo"}}','f4')</code></p>
     * <p>Example result: <code>{"f5":99,"f6":"foo"}</code></p>
     *
     * @param jsonField The JSON {@code Field} to extract the path from
     * @param path      Path to the the object to return
     * @return A {@code Field} representing the object at the specified path
     * @see #objectAtPath(Field, String...)
     * @see #objectAtPath(Field, Collection)
     * @see #extractPath(Field, String...)
     */
    public static Field<Jsonb> extractPath(Field<Jsonb> jsonField, Collection<String> path) {
        return extractPath(jsonField, path.toArray(new String[0]));
    }

    /**
     * <p>Returns JSON value pointed to by {@code path} as text (equivalent to <code>#&gt;&gt;</code> operator, ie.
     * {@link #objectAtPathText(Field, String...)}).</p>
     *
     * <p>Example: <code>jsonb_extract_path_text('{"f2":{"f3":1},"f4":{"f5":99,"f6":"foo"}}','f4', 'f6')</code></p>
     * <p>Example result: <code>foo</code></p>
     *
     * @param jsonField The JSON {@code Field} to extract the path from
     * @param path      Path to the the object to return
     * @return A {@code Field} representing the object at the specified path, as text
     * @see #objectAtPathText(Field, String...)
     * @see #objectAtPathText(Field, Collection)
     * @see #extractPathText(Field, Collection)
     */
    public static Field<String> extractPathText(Field<Jsonb> jsonField, String... path) {
        return DSL.field("jsonb_extract_path_text({0}, VARIADIC {1})", String.class, jsonField, DSL.array(path));
    }

    /**
     * <p>Returns JSON value pointed to by {@code path} as text (equivalent to <code>#&gt;&gt;</code> operator, ie.
     * {@link #objectAtPathText(Field, Collection)}).</p>
     *
     * <p>Example: <code>jsonb_extract_path_text('{"f2":{"f3":1},"f4":{"f5":99,"f6":"foo"}}','f4', 'f6')</code></p>
     * <p>Example result: <code>foo</code></p>
     *
     * @param jsonField The JSON {@code Field} to extract the path from
     * @param path      Path to the the object to return
     * @return A {@code Field} representing the object at the specified path, as text
     * @see #objectAtPathText(Field, String...)
     * @see #objectAtPathText(Field, Collection)
     * @see #extractPathText(Field, String...)
     */
    public static Field<String> extractPathText(Field<Jsonb> jsonField, Collection<String> path) {
        return extractPathText(jsonField, path.toArray(new String[0]));
    }

    /**
     * <p>Returns the type of the outermost JSON value as a text string. Possible types are {@code object}, {@code array},
     * {@code string}, {@code number}, {@code boolean}, and {@code null}.</p>
     *
     * <p>Example: <code>jsonb_typeof('-123.4')</code></p>
     * <p>Example result: <code>number</code></p>
     *
     * @param jsonField The JSON {@code Field} to determine the type of
     * @return The JSON type
     */
    public static Field<String> typeOf(Field<Jsonb> jsonField) {
        return DSL.field("jsonb_typeof({0})", String.class, jsonField);
    }

    /**
     * <p>Returns a JSON {@code Field} with all object fields that have {@code null} values omitted. Other {@code null}
     * values (eg. in arrays) are untouched.</p>
     *
     * <p>Example: <code>jsonb_strip_nulls('[{"f1":1,"f2":null},2,null,3]')</code></p>
     * <p>Example result: <code>[{"f1":1},2,null,3]</code></p>
     *
     * @param jsonField The JSON {@code Field} to remove {@code null} values from
     * @return A JSON {@code Field} with {@code null} object fields removed
     */
    public static Field<Jsonb> stripNulls(Field<Jsonb> jsonField) {
        return DSL.field("jsonb_strip_nulls({0})", Jsonb.class, jsonField);
    }

    /**
     * <p>Returns the JSON {@code Field} as indented JSON text.</p>
     *
     * <p>Example: <code>jsonb_pretty('[{"f1":1,"f2":null},2,null,3]')</code></p>
     * <p>Example result: </p><pre>{@code
     * [
     *     {
     *         "f1": 1,
     *         "f2": null
     *     },
     *     2,
     *     null,
     *     3
     * ]
     * }</pre>
     *
     * @param jsonField The JSON {@code Field} to format
     * @return Pretty formatted, intended String representation of the JSON {@code Field}
     */
    public static Field<String> pretty(Field<Jsonb> jsonField) {
        return DSL.field("jsonb_pretty({0})", String.class, jsonField);
    }
}
