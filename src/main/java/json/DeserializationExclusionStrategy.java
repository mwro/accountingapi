package json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class DeserializationExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes field) {
        return field.getAnnotation(DeserializeExclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
