package com.crestani.bolsaDeValoresJetty;

public class ControlMessage {

	enum ControlMessageCode {
		ACK, NACK
	}

	private ControlMessageCode code;

	public ControlMessage(ControlMessageCode code) {
		this.code = code;
	}

	public String toJson() {
		if (code == ControlMessageCode.ACK) {
			return "{code: ACK}";
		} else if (code == ControlMessageCode.NACK) {
			return "{code: NACK}";
		} else {
			return "{code: }";
		}
	}

}
