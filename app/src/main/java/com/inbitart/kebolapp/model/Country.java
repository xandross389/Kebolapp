package com.inbitart.kebolapp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by XandrOSS on 05/08/2016.
 */
@DatabaseTable(tableName = "countries")
public class Country {
    // columns names
    public static final String ID_FIELD_NAME = "id";
    public static final String NAME_FIELD_NAME = "name";
    public static final String CODE_FIELD_NAME = "code";
    public static final String DESCRIPTION_FIELD_NAME = "description";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private Integer id;
    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String countryName;
    @DatabaseField(columnName = CODE_FIELD_NAME)
    private String countryCode;
    @DatabaseField(columnName = DESCRIPTION_FIELD_NAME)
    private String countryDescription;

    public Country(String countryName, String countryCode, String countryDescription) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.countryDescription = countryDescription;
    }

    public Country(String countryName, String countryCode) {
        this.countryName = countryName;
        this.countryCode = countryCode;
    }

    public Country() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return countryName;
    }

    public void setName(String countryName) {
        this.countryName = countryName;
    }

    public String getCode() {
        return countryCode;
    }

    public void setCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDescription() {
        return countryDescription;
    }

    public void setDescription(String countryDescription) {
        this.countryDescription = countryDescription;
    }
}
