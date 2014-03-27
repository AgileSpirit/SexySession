package io.jrocket.infra.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.rules.ExpectedException;

public class JsonUtilsTest {

    private final static String SOME_KEY = "some_key";
    private final static String SOME_VALUE = "some_value";
    private final static String ANOTHER_KEY = "another_key";
    private final static String ANOTHER_VALUE = "another_value";

    /*
     * JsonUtils#set(...)
     */

    @Test
    public void givenDataAndPropertyAreOk_whenSetPropertyToData_thenReturnDataWithAddedProperty() throws ApplicationException {
        // Given
        String data = "{ \"" + SOME_KEY + "\" : \"" + SOME_VALUE + "\" }";
        JsonUtils.JsonProperty property = new JsonUtils.JsonProperty(ANOTHER_KEY, ANOTHER_VALUE);

        // When
        String actual = JsonUtils.set(data, property);

        // Expected
        String expected = "{" +
                "\"" + SOME_KEY + "\":\"" + SOME_VALUE + "\"," +
                "\"" + ANOTHER_KEY + "\":\"" + ANOTHER_VALUE + "\"" +
                "}";
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void givenDataIsNullAndPropertyIsOk_whenSetPropertyToData_thenReturnNewDataStringWithAddedProperty() throws ApplicationException {
        // Given
        String data = null;
        JsonUtils.JsonProperty property = new JsonUtils.JsonProperty(ANOTHER_KEY, ANOTHER_VALUE);

        // When
        String actual = JsonUtils.set(data, property);

        // Expected
        String expected = "{" +
                "\"" + ANOTHER_KEY + "\":\"" + ANOTHER_VALUE + "\"" +
                "}";
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = ApplicationException.class)
    public void givenDataIsOkAndPropertyKeyIsNull_whenSetPropertyToData_thenThrowsApplicationException() throws ApplicationException {
        // Given
        String data = "{ \"" + SOME_KEY + "\" : \"" + SOME_VALUE + "\" }";
        JsonUtils.JsonProperty property = new JsonUtils.JsonProperty(null, ANOTHER_VALUE);

        // When
        String actual = JsonUtils.set(data, property);
    }

    @Test(expected = ApplicationException.class)
    public void givenDataIsOkAndPropertyValueIsNull_whenSetPropertyToData_thenThrowsApplicationException() throws ApplicationException {
        // Given
        String data = "{ \"" + SOME_KEY + "\" : \"" + SOME_VALUE + "\" }";
        JsonUtils.JsonProperty property = new JsonUtils.JsonProperty(ANOTHER_KEY, null);

        // When
        String actual = JsonUtils.set(data, property);
    }

    @Test
    public void givenPropertyKeyAlreadyExistsInData_whenSetPropertyToData_thenReturnDataWithUpdatedProperty() throws ApplicationException {
        // Given
        String data = "{ \"" + SOME_KEY + "\" : \"" + SOME_VALUE + "\" }";
        JsonUtils.JsonProperty property = new JsonUtils.JsonProperty(SOME_KEY, ANOTHER_VALUE);

        // When
        String actual = JsonUtils.set(data, property);

        // Expected
        String expected = "{" +
                "\"" + SOME_KEY + "\":\"" + ANOTHER_VALUE + "\"" +
                "}";
    }

    private final void testSet(String data, JsonUtils.JsonProperty property, String expected) throws ApplicationException {
        // When
        String actual = JsonUtils.set(data, property);

        // Then
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
