package model;

import java.util.Date;

public class ProofOfDelivery {
    private final String signer;
    private final String code;
    private final Date timestamp;

    public ProofOfDelivery(String signer, String code, Date timestamp){
        this.signer = signer;
        this.code = code;
        this.timestamp = timestamp;
    }

    public String getSigner(){ return signer; }
    public String getCode(){ return code; }
    public Date getTimestamp(){ return timestamp; }

    public boolean isValid(){
        return signer != null && !signer.isEmpty() && code != null && !code.isEmpty();
    }

    @Override public String toString(){
        return String.format("POD by %s @ %s (code:%s)", signer, timestamp, code);
    }
}