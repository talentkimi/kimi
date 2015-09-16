package core.net.ftp;

class FtpCode {

	private final String code;

	public int get() {
		return Integer.parseInt(code);
	}

	public boolean isPositivePreliminary() {
		return code.charAt(0) == '1';
	}

	public boolean isPositiveCompletion() {
		return code.charAt(0) == '2';
	}

	public boolean isPositiveIntermediate() {
		return code.charAt(0) == '3';
	}

	public boolean isTransientNegativeCompletion() {
		return code.charAt(0) == '4';
	}

	public boolean isPermanentNegativeCompletion() {
		return code.charAt(0) == '5';
	}

	public boolean isSyntaxError() {
		return code.charAt(1) == '0';
	}

	public boolean isRequestForInformation() {
		return code.charAt(1) == '1';
	}

	public boolean isConnectionRelated() {
		return code.charAt(1) == '2';
	}

	public boolean isAuthorizationRelated() {
		return code.charAt(1) == '3';
	}

	public boolean isFileRelated() {
		return code.charAt(1) == '5';
	}

	public boolean equals(Object object) {
		if (object != null) {
			return this.code.equals(object.toString());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	public FtpCode(int code) {
		this.code = String.valueOf(code);
		if (this.code.length() != 3) {
			throw new IllegalArgumentException("code=" + code);
		}
	}
}