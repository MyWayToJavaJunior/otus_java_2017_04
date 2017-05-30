import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.otus.homework08.json.serializer.JSONSerializer;
import ru.otus.homework08.working.classes.SerializebleObject;

public class JSONSerializerTest {
    private JSONSerializer serializer;
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
            jsonObject = serializer.objectToJSON(serializebleObject);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertTrue(jsonObject != null && jsonObject.size() == 15);
    }

    @Test
    public void deSerializeSimpleFieldsTest() {
        JSONObject jsonObject = serializer.objectToJSON(serializebleObject);

        SerializebleObject deSerializebleObject = new SerializebleObject();
        try {
            serializer.JSONToObject(jsonObject, deSerializebleObject);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertTrue(serializebleObject.simpleFieldsEquals(deSerializebleObject));
    }

    @Test
    public void deSerializeObjectFieldTest() {
        JSONObject jsonObject = serializer.objectToJSON(serializebleObject);

        SerializebleObject deSerializebleObject = new SerializebleObject();
        try {
            serializer.JSONToObject(jsonObject, deSerializebleObject);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertTrue(serializebleObject.ojbectFieldsEquals(deSerializebleObject));
    }

    @Test
    public void deSerializeArrayFieldsTest() {
        JSONObject jsonObject = serializer.objectToJSON(serializebleObject);

        SerializebleObject deSerializebleObject = new SerializebleObject();
        try {
            serializer.JSONToObject(jsonObject, deSerializebleObject);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertTrue(serializebleObject.arrayFieldsEquals(deSerializebleObject));
    }

    @Test
    public void deSerializeListFieldsTest() {
        JSONObject jsonObject = serializer.objectToJSON(serializebleObject);

        SerializebleObject deSerializebleObject = new SerializebleObject();
        try {
            serializer.JSONToObject(jsonObject, deSerializebleObject);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertTrue(serializebleObject.listsFieldsEquals(deSerializebleObject));
    }

    @Test
    public void deSerializeSetFieldsTest() {
        JSONObject jsonObject = serializer.objectToJSON(serializebleObject);

        SerializebleObject deSerializebleObject = new SerializebleObject();
        try {
            serializer.JSONToObject(jsonObject, deSerializebleObject);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertTrue(serializebleObject.setFieldsEquals(deSerializebleObject));
    }

    @Test
    public void deSerializeMapFieldsTest() {
        JSONObject jsonObject = serializer.objectToJSON(serializebleObject);

        SerializebleObject deSerializebleObject = new SerializebleObject();
        try {
            serializer.JSONToObject(jsonObject, deSerializebleObject);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertTrue(serializebleObject.mapFieldsEquals(deSerializebleObject));
    }
    @Test
    public void deSerializeTest() {
        JSONObject jsonObject = serializer.objectToJSON(serializebleObject);

        SerializebleObject deSerializebleObject = new SerializebleObject();
        try {
            serializer.JSONToObject(jsonObject, deSerializebleObject);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertTrue(serializebleObject.equals(deSerializebleObject));
    }



}
