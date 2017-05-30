import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.otus.homework08.json.serializer.IJSONSerializer;
import ru.otus.homework08.json.serializer.JSONSerializer;
import ru.otus.homework08.working.classes.SerializebleObject;

public class JSONSerializerTest {
    private IJSONSerializer serializer;
    private SerializebleObject serializebleObject;

    @Before
    public void init() {
        serializer = new JSONSerializer();
        serializebleObject = new SerializebleObject();
        serializebleObject.fillPrivateCollectionsByFakeData();
    }

    @Test
    public void serializeTest() {
        JSONObject jsonObject = null;
        try {
            jsonObject = serializer.serializeObjectToJSON(serializebleObject);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertTrue(jsonObject != null && jsonObject.size() == 15);
    }

    @Test
    public void deSerializeSimpleFieldsTest() {
        JSONObject jsonObject = serializer.serializeObjectToJSON(serializebleObject);

        try {
            SerializebleObject deSerializebleObject = (SerializebleObject) serializer.deserializeJSONToObject(jsonObject, SerializebleObject.class);
            Assert.assertTrue(serializebleObject.simpleFieldsEquals(deSerializebleObject));
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void deSerializeObjectFieldTest() {
        JSONObject jsonObject = serializer.serializeObjectToJSON(serializebleObject);

        try {
            SerializebleObject deSerializebleObject = (SerializebleObject) serializer.deserializeJSONToObject(jsonObject, SerializebleObject.class);
            Assert.assertTrue(serializebleObject.ojbectFieldsEquals(deSerializebleObject));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void deSerializeArrayFieldsTest() {
        JSONObject jsonObject = serializer.serializeObjectToJSON(serializebleObject);

        try {
            SerializebleObject deSerializebleObject = (SerializebleObject) serializer.deserializeJSONToObject(jsonObject, SerializebleObject.class);
            Assert.assertTrue(serializebleObject.arrayFieldsEquals(deSerializebleObject));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void deSerializeListFieldsTest() {
        JSONObject jsonObject = serializer.serializeObjectToJSON(serializebleObject);

        try {
            SerializebleObject deSerializebleObject = (SerializebleObject) serializer.deserializeJSONToObject(jsonObject, SerializebleObject.class);
            Assert.assertTrue(serializebleObject.listsFieldsEquals(deSerializebleObject));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void deSerializeSetFieldsTest() {
        JSONObject jsonObject = serializer.serializeObjectToJSON(serializebleObject);

        try {
            SerializebleObject deSerializebleObject = (SerializebleObject) serializer.deserializeJSONToObject(jsonObject, SerializebleObject.class);
            Assert.assertTrue(serializebleObject.setFieldsEquals(deSerializebleObject));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void deSerializeMapFieldsTest() {
        JSONObject jsonObject = serializer.serializeObjectToJSON(serializebleObject);

        try {
            SerializebleObject deSerializebleObject = (SerializebleObject) serializer.deserializeJSONToObject(jsonObject, SerializebleObject.class);
            Assert.assertTrue(serializebleObject.mapFieldsEquals(deSerializebleObject));
        } catch (Exception e) {
            Assert.fail();
        }
    }
    @Test
    public void deSerializeTest() {
        JSONObject jsonObject = serializer.serializeObjectToJSON(serializebleObject);

        try {
            SerializebleObject deSerializebleObject = (SerializebleObject) serializer.deserializeJSONToObject(jsonObject, SerializebleObject.class);
            Assert.assertTrue(serializebleObject.equals(deSerializebleObject));
        } catch (Exception e) {
            Assert.fail();
        }
    }



}
