package com.example.bluehorsesoftkol.ekplatevendor.bean;

/**
 * Created by BLUEHORSE 123 on 10/30/2015.
 */
public class SupervisorInformationItem {

    private String _senderImagePath;
    private String _supervisorName;
    private String _supervisornotes;
    private String _time;
    private String _sender;

    public String getSenderImagePath() {
        return _senderImagePath;
    }

    public void setSenderImagePath(String _senderImagePath) {
        this._senderImagePath = _senderImagePath;
    }

    public String getSupervisorName() {
        return _supervisorName;
    }

    public void setSupervisorName(String _supervisorName) {
        this._supervisorName = _supervisorName;
    }

    public String getSupervisorNotes() {
        return _supervisornotes;
    }

    public void setSupervisorNotes(String _supervisornotes) {
        this._supervisornotes = _supervisornotes;
    }

    public String getTime() {
        return _time;
    }

    public void setTime(String _time) {
        this._time = _time;
    }


    public String getSender() {
        return _sender;
    }

    public void setSender(String _sender) {
        this._sender = _sender;
    }
}
