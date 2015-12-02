package com.example.shihaoking.myapplication.service;

import android.os.Bundle;
import android.os.Message;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2015/12/2.
 */
public class SoapService {
    private List<PropertyInfo> propertyInfos;
    private SoapObject request;
    private HttpTransportSE httpTransportSE;
    private SoapSerializationEnvelope envelope;
    private String namespace;
    private String methodName;
    private String url;

    public SoapService() {
        propertyInfos = new ArrayList<PropertyInfo>();
    }

    public SoapService(String url, String namespace, String methodName) {
        this.namespace = namespace;
        this.methodName = methodName;
        this.url = url;
        propertyInfos = new ArrayList<PropertyInfo>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void addRequestPropertyInfo(String name, Object value){
        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.setName(name);
        propertyInfo.setValue(value);
        propertyInfo.setType(value.getClass());
        propertyInfos.add(propertyInfo);
    }

    public SoapObject buildRequestSoap(){
        this.request = new SoapObject(namespace, methodName);
        for (PropertyInfo pi: propertyInfos) {
            this.request.addProperty(pi);
        }

        return this.request;
    }

    public SoapSerializationEnvelope buildEnvelope() {
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(this.request);

        return envelope;
    }

    public SoapObject requestResult(SoapSerializationEnvelope envelope) throws IOException, XmlPullParserException {
        httpTransportSE = new HttpTransportSE(this.url);

        httpTransportSE.call(this.namespace + methodName, envelope);

        if (envelope == null) {
            return null;
        } else {
            return (SoapObject) envelope.bodyIn;
        }
    }

    public Object requestResult() throws IOException, XmlPullParserException {
        buildRequestSoap();
        buildEnvelope();

        httpTransportSE = new HttpTransportSE(this.url);

        httpTransportSE.call(this.namespace + this.methodName, this.envelope);

        if (envelope == null) {
            return null;
        } else {
            return envelope.getResponse();
        }
    }
}
