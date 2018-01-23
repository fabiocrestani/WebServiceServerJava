package com.crestani.bolsaDeValoresJetty;

public class ControlMessage {

	enum ControlMessageCode {
		ACK, NACK
	}

	private ControlMessageCode code;

	public ControlMessage(ControlMessageCode code) {
		this.code = code;
	}

	public ControlMessageCode getCode() {
		return code;
	}

}
