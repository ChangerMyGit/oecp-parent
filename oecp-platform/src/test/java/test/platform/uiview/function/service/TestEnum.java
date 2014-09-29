package test.platform.uiview.function.service;

public enum TestEnum implements ChartTypeEnum {
	T("TTTT"),X("XXX");
	
	private String v = null;
	private TestEnum(String s) {
		this.v = s;
	}
	@Override
	public String getFactoryName() {
		return this.v;
	}

}
