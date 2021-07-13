package com.portableehr.sdk.network.NAO.inbound;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.util.Date;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-03
 *
 * Copyright Portable Ehr Inc, 2019
 */

public class IBLabRequest {
    private IBLab lab;
    private String guid;
    private IBPractitioner practitioner;
    private String instructions;
    private String prescriberNote;
    private Date createdOn;
    private Date requestDate;
    private String status;
    private String labRequestEid;
    private Date lastUpdated;
    private IBLabRequestPDFdocument pdf;
    private IBLabRequestTextDocument text;

    public IBLab getLab() {
        return lab;
    }

    public void setLab(IBLab lab) {
        this.lab = lab;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public IBPractitioner getPractitioner() {
        return practitioner;
    }

    public void setPractitioner(IBPractitioner practitioner) {
        this.practitioner = practitioner;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getPrescriberNote() {
        return prescriberNote;
    }

    public void setPrescriberNote(String prescriberNote) {
        this.prescriberNote = prescriberNote;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLabRequestEid() {
        return labRequestEid;
    }

    public void setLabRequestEid(String labRequestEid) {
        this.labRequestEid = labRequestEid;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public IBLabRequestPDFdocument getPdf() {
        return pdf;
    }

    public void setPdf(IBLabRequestPDFdocument pdf) {
        this.pdf = pdf;
    }

    public IBLabRequestTextDocument getText() {
        return text;
    }

    public void setText(IBLabRequestTextDocument text) {
        this.text = text;
    }

    //*********************************************************************************************/
    //** GSON helper                                                                             **/
    //*********************************************************************************************/

    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static IBLabRequest fromJson(String json) {
        GsonBuilder  builder          = GsonFactory.standardBuilder();
        Gson         jsonDeserializer = builder.create();
        IBLabRequest theObject        = jsonDeserializer.fromJson(json, IBLabRequest.class);
        return theObject;
    }
}
