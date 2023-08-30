package test.java.utils.helpers;

import io.restassured.path.json.JsonPath;

import java.util.ArrayList;

public class HelperMethods {


    public ArrayList getStateCode(JsonPath jp) {
        ArrayList stateCode = jp.get("data.state_code");
        return stateCode;
    }
}

