package com.portableehr.sdk.network.NAO.inbound;

import android.util.Base64;
import android.util.Log;

import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.Date;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2017-06-14
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBPrivateMessage {

    private String subject;
    private String from;
    private String patient;
    private String to;
    private Date   date;
    private String messageB64;
    private String source;
    private String documentB64;
    private String documentType;
    private String documentName;

    private IBPrivateMessage() {
        onNew();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessageB64() {
        return messageB64;
    }

    public void setMessageB64(String messageB64) {
        this.messageB64 = messageB64;
    }

    public String getMessage() {
        String b64     = this.getMessageB64();
        String message = "";
        if (b64 != null) {
            try {
                byte[] bytes        = b64.getBytes();
                byte[] messageBytes = Base64.decode(bytes, 0);
                message = new String(messageBytes, "UTF-8");
//                message = new String(messageBytes, "ISO-8859-1");
            } catch (Exception e) {
                Log.wtf(TAG, "getMessage: Caucht exception while bessageB64", e);
            }
            return message;

        } else {
            return null;
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDocumentB64() {
        return documentB64;
    }

    public void setDocumentB64(String documentB64) {
        this.documentB64 = documentB64;
    }

    public byte[] getDocument() {
        String b64   = this.getDocumentB64();
        byte[] messageBytes;
        if (b64 != null) {
            try {
                byte[] bytes        = b64.getBytes();
                messageBytes = Base64.decode(bytes, 0);
            } catch (Exception e) {
                Log.wtf(TAG, "getMessage: Caucht exception while bessageB64", e);
                return null;
            }
            return messageBytes;
        } else {
            return null;
        }
    }

    public boolean hasDocument(){
        return this.getDocumentB64()!=null;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + IBPrivateMessage.class.getSimpleName();
    @GSONexcludeOutbound
    private              String  TAG;
    private static       int     lifeTimeInstances;
    private static       int     numberOfInstances;
    @GSONexcludeOutbound
    private              int     instanceNumber;
    @GSONexcludeOutbound
    private static       boolean classCountable = false;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        numberOfInstances--;
        if (numberOfInstances < 0) {
            Log.e(getLogTAG(), "*** You did not call onNew in your ctor(s)");
        }
        if (classCountable) {
            Log.d(getLogTAG(), "finalize  ");
        }
    }

    protected void onNew() {
        TAG = CLASSTAG;
        numberOfInstances++;
        lifeTimeInstances++;
        instanceNumber = lifeTimeInstances;
        if (classCountable) {
            Log.d(getLogTAG(), "onNew ");
        }
    }

    private String getLogLabel() {
        return Integer.toHexString(instanceNumber) + "/" + Integer.toHexString(numberOfInstances);
    }

    public static  void setClassCountable( boolean isIt) {
        classCountable = isIt;
    }

    private String getLogTAG() {
        TAG = CLASSTAG + " [" + getLogLabel() + "] ";
        return TAG;
    }

    //endregion

}
