package oecp.framework.util.soap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * 通过cxf Client 调用webservices
 * 
 * @author wangliang
 * @date 2011-8-9下午03:48:30
 * @version 1.0
 */
public class SoapUtils {
	/**
	 * 通过url调用webservice 获取返回值
	 * 
	 * @author wangliang
	 * @date 2011-8-10上午10:47:02
	 * @param wsUrl
	 * @param method
	 * @param arg
	 * @return
	 */
	public static Object[] callService(String wsUrl, String method,
			Object... arg) throws Exception {
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient(wsUrl);
		Object[] res = null;
		res = client.invoke(method, arg);
		return res;
	}

	/**
	 * 获取服务方法
	 * 
	 * @author wangliang
	 * @date 2011-8-10上午09:51:09
	 * @param wsUrl
	 * @return
	 * @throws WSDLException
	 */
	public static String[] getServiceOperations(Definition def)
			throws WSDLException {
		List<String> ops = new ArrayList<String>();
		ArrayList<Service> services = getServices(def);
		for (Service service : services) {
			// 解析接口方法名
			Map<?, ?> ports = service.getPorts();
			Set<?> port_set = ports.keySet();
			for (Iterator<?> iterator = port_set.iterator(); iterator.hasNext();) {
				String portName = (String) iterator.next();
				Port port = service.getPort(portName);
				Binding binding = port.getBinding();
				PortType portType = binding.getPortType();
				List<?> operations = portType.getOperations();
				Iterator<?> operIter = operations.iterator();
				while (operIter.hasNext()) {
					Operation operation = (Operation) operIter.next();
					if (!operation.isUndefined()) {
						ops.add(operation.getName());
					}
				}
			}
		}
		String[] ops_str = new String[ops.size()];
		ops_str = ops.toArray(ops_str);
		return ops_str;
	}

	public static String[] getServiceOperationsDesc(Definition def)
			throws WSDLException {
		List<String> ops = new ArrayList<String>();
		ArrayList<Service> services = getServices(def);
		for (Service service : services) {
			// 解析接口方法名
			Map<?, ?> ports = service.getPorts();
			Set<?> port_set = ports.keySet();
			for (Iterator<?> iterator = port_set.iterator(); iterator.hasNext();) {
				String portName = (String) iterator.next();
				Port port = service.getPort(portName);
				Binding binding = port.getBinding();
				PortType portType = binding.getPortType();
				List<?> operations = portType.getOperations();
				Iterator<?> operIter = operations.iterator();
				while (operIter.hasNext()) {
					Operation operation = (Operation) operIter.next();
					if (!operation.isUndefined()) {
						ops.add(operation.getDocumentationElement()
								.getTextContent());
					}
				}
			}
		}
		String[] ops_str = new String[ops.size()];
		ops_str = ops.toArray(ops_str);
		return ops_str;
	}

	public static ArrayList<Service> getServices(Definition def) {
		ArrayList<Service> list = new ArrayList<Service>();
		Map<?, ?> services = def.getServices();
		Set<?> key = services.keySet();
		for (Iterator<?> it = key.iterator(); it.hasNext();) {
			QName qname = (QName) it.next();
			list.add(def.getService(qname));
		}
		return list;
	}

	/**
	 * 读取wsdl定义
	 * 
	 * @author wangliang
	 * @date 2011-8-10上午10:45:38
	 * @param wsUrl
	 * @return
	 * @throws WSDLException
	 */
	public static Definition getDefinition(String wsUrl) throws WSDLException {
		WSDLFactory factory = WSDLFactory.newInstance();
		WSDLReader reader = factory.newWSDLReader();
		reader.setFeature("javax.wsdl.verbose", true);
		reader.setFeature("javax.wsdl.importDocuments", true);
		return reader.readWSDL(wsUrl);
	}

	/**
	 * 获取服务名
	 * 
	 * @author lintao
	 * @date 2011-8-15上午10:35:43
	 * @param
	 * @return
	 */
	public static String getServiceName(Definition def) {
		ArrayList<Service> services = getServices(def);
		// 用来判断一个描述文件是否只描述一个服务
		StringBuffer names = new StringBuffer();
		for (int i = services.size() - 1; i >= 0; i--) {
			names.append(services.get(i).getQName().getLocalPart());
			if (i != 0) {
				names.append(",");
			}
		}
		return names.toString();

	}

	/**
	 * 获取服务描述
	 * 
	 * @author lintao
	 * @date 2011-8-15上午10:35:43
	 * @param
	 * @return
	 */
	public static String getServiceDesc(Definition def) {
		ArrayList<Service> services = getServices(def);
		// 用来判断一个描述文件是否只描述一个服务
		StringBuffer names = new StringBuffer();
		for (int i = services.size() - 1; i >= 0; i--) {
			names.append(services.get(i).getDocumentationElement()
					.getTextContent());
			if (i != 0) {
				names.append(",");
			}
		}
		return names.toString();
	}

	public static void main(String[] args) throws WSDLException {
		Definition def = SoapUtils
				.getDefinition("http://localhost:8080/oecp_bc/BCRegisterBBBBB?wsdl");
		System.out.println(def.getQName().getLocalPart());
		System.out.println(def.getQName().getNamespaceURI());
	}
}
