package com.jpragma.snmp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jpragma.snmp.asn.AsnNull;
import com.jpragma.snmp.asn.AsnOID;
import com.jpragma.snmp.asn.AsnObject;
import com.jpragma.snmp.asn.AsnObjectValueException;
import com.jpragma.snmp.asn.ber.BerTlv;
import com.jpragma.snmp.types.GetNextRequestPDU;
import com.jpragma.snmp.types.GetRequestPDU;
import com.jpragma.snmp.types.GetResponsePDU;
import com.jpragma.snmp.types.Message;
import com.jpragma.snmp.types.PDU;
import com.jpragma.snmp.types.SetRequestPDU;
import com.jpragma.snmp.types.VarBind;
import com.jpragma.snmp.types.VarBindList;

public class SnmpClient {

    private Log log = LogFactory.getLog(this.getClass());
    private static final int PACKET_MAX_SIZE = 484;
    private static final int DEAFULT_PORT = 161;
    private static final String DEFAULT_COMMUNITY = "public";
    private static final int DEFAULT_TIMEOUT = 5000;
    private DatagramSocket socket;
    private InetAddress agentAddress;
    private int agentPort;
    private String readOnlyCommunity;
    private String readWriteCommunity;
    private int requestId;

    public SnmpClient() {
        requestId = 0;
        try {
            agentAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new SnmpClientException("Can't obtain localhost address", e);
        }
        agentPort = 161;
        readOnlyCommunity = "public";
        readWriteCommunity = "public";
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(5000);
        } catch (Exception e) {
            throw new SnmpClientException(e);
        }
    }

    public SnmpClient(String agentAddress, int agentPort, int timeout, String readCommunity, String writeCommunity) {
        requestId = 0;
        try {
            this.agentAddress = InetAddress.getByName(agentAddress);
        } catch (UnknownHostException e) {
            throw new SnmpClientException("Can't obtain address for host " + agentAddress, e);
        }
        this.agentPort = agentPort;
        readOnlyCommunity = readCommunity;
        readWriteCommunity = writeCommunity;
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(timeout);
        } catch (Exception e) {
            throw new SnmpClientException(e);
        }
    }

    public SnmpClient(String agentAddress, int agentPort, String readCommunity, String writeCommunity) {
        this(agentAddress, agentPort, 5000, readCommunity, writeCommunity);
    }

    public String getAgentAddress() {
        return agentAddress.getHostAddress();
    }

    public void setAgentAddress(String agentAddress) {
        try {
            this.agentAddress = InetAddress.getByName(agentAddress);
        } catch (UnknownHostException e) {
            throw new SnmpClientException("Can't obtain localhost address", e);
        }
    }

    public int getAgentPort() {
        return agentPort;
    }

    public void setAgentPort(int agentPort) {
        this.agentPort = agentPort;
    }

    public int getTimeout() {
        try {
            return socket != null ? socket.getSoTimeout() : 5000;
        } catch (SocketException e) {
            throw new SnmpClientException(e);
        }
    }

    public void setTimeout(int timeout) {
        if (socket != null) {
            try {
                socket.setSoTimeout(timeout);
            } catch (SocketException e) {
                throw new SnmpClientException(e);
            }
        }
    }

    public String getReadOnlyCommunity() {
        return readOnlyCommunity;
    }

    public void setReadOnlyCommunity(String readOnlyCommunity) {
        this.readOnlyCommunity = readOnlyCommunity;
    }

    public String getReadWriteCommunity() {
        return readWriteCommunity;
    }

    public void setReadWriteCommunity(String readWriteCommunity) {
        this.readWriteCommunity = readWriteCommunity;
    }

    public Message snmpget(String oid) throws SocketTimeoutException {
        log.debug("Sending snmpget for oid:" + oid);
        AsnOID requestOID = createRequestOid(oid);
        VarBind varBind = new VarBind(requestOID, new AsnNull());
        VarBindList varBindList = new VarBindList();
        varBindList.add(varBind);
        GetRequestPDU requestPDU = new GetRequestPDU(requestId, varBindList);
        Message request = new Message(0, readOnlyCommunity, requestPDU);
        return snmpOperation(request);
    }

    public Message snmpgetnext(String oid) throws SocketTimeoutException {
        log.debug("Sending snmpgetnext for oid:" + oid);
        AsnOID requestOID = createRequestOid(oid);
        VarBind varBind = new VarBind(requestOID, new AsnNull());
        VarBindList varBindList = new VarBindList();
        varBindList.add(varBind);
        GetNextRequestPDU requestPDU = new GetNextRequestPDU(requestId, varBindList);
        Message request = new Message(0, readOnlyCommunity, requestPDU);
        return snmpOperation(request);
    }

    public Message snmpset(String oid, AsnObject value) throws SocketTimeoutException {
        log.debug("Sending snmpset for oid:" + oid);
        AsnOID requestOID = createRequestOid(oid);
        VarBind varBind = new VarBind(requestOID, value);
        VarBindList varBindList = new VarBindList();
        varBindList.add(varBind);
        SetRequestPDU requestPDU = new SetRequestPDU(requestId, varBindList);
        Message request = new Message(0, readWriteCommunity, requestPDU);
        return snmpOperation(request);
    }

    private AsnOID createRequestOid(String oid) {
        try {
            return new AsnOID(oid);
        } catch (AsnObjectValueException e) {
            log.error("Error creating AsnOID. Provided String " + oid + " is not valid");
            throw new SnmpClientException(e);
        }
    }

    protected Message snmpOperation(Message request) throws SocketTimeoutException {
        long messageRequestId = ((PDU) request.getPDU()).getRequestId();
        byte requestData[] = request.toBerTlv().getBytes();
        DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, agentAddress, agentPort);
        try {
            socket.send(requestPacket);
        } catch (IOException e) {
            throw new SnmpClientException("Error sending a packet to SNMP agent.");
        }
        Message response = null;
        GetResponsePDU responsePDU;

        do {
            byte responseData[] = new byte[484];
            DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);
            try {
                socket.receive(responsePacket);
            } catch (SocketTimeoutException e) {
                throw e;
            } catch (IOException e) {
                throw new SnmpClientException("Error receiving a packet from SNMP agent.");
            }
            responseData = responsePacket.getData();
            BerTlv responseTLV = new BerTlv();
            responseTLV.decode(responseData);
            response = new Message();
            response.setValue(responseTLV);
            responsePDU = (GetResponsePDU) response.getPDU();
        } while (responsePDU.getRequestId() != messageRequestId);

        log.debug("Received snmp response: " + response.toString());
        return response;
    }

}
