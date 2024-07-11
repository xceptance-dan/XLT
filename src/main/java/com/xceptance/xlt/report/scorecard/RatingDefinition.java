package com.xceptance.xlt.report.scorecard;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("rating")
public class RatingDefinition
{
    @XStreamAsAttribute
    private final String id;

    @XStreamAsAttribute
    private final String name;

    private final String description;

    @XStreamAsAttribute
    private final double value;

    @XStreamAsAttribute
    private final boolean enabled;

    @XStreamAsAttribute
    private final boolean failsTest;

    RatingDefinition(final String id, final String name, final String description, final double value, final boolean enabled,
                     final boolean failsTest)
    {
        this.id = Objects.requireNonNull(id, "Rating ID must not be null");
        this.name = name;
        this.value = value;
        this.description = description;
        this.enabled = enabled;
        this.failsTest = failsTest;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public double getValue()
    {
        return value;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public boolean isFailsTest()
    {
        return failsTest;
    }

    static RatingDefinition fromJSON(final JSONObject jsonObject) throws ValidationException
    {
        final String id = jsonObject.getString("id");
        final String name = StringUtils.trimToNull(jsonObject.optString("name"));
        final String desc = jsonObject.optString("description", null);
        final double value = jsonObject.getDouble("value");
        final boolean enabled = jsonObject.optBoolean("enabled", true);
        final boolean failsTest = jsonObject.optBoolean("failsTest", false);

        if (Math.min(Math.max(0.0, value), 100.0) != value)
        {
            throw new ValidationException("Property 'value' must be greater than or equal to 0.0 and less than or equal to 100.0");
        }

        return new RatingDefinition(id, name, desc, value, enabled, failsTest);
    }
}
