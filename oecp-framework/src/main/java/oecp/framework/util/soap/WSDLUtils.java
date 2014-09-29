/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.framework.util.soap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.reader.SchemaReader;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.ibm.wsdl.extensions.schema.SchemaImpl;


/**
 * WSDL工具类
 * 
 * @author slx
 * @date 2011-8-18上午11:38:33
 * @version 1.0
 */
public class WSDLUtils {

	public static final String _SCHEMA = "schema";
	
	public static void main(String[] args) throws Exception {
		String strwsdl = getFileContent("c://1.wsdl");
		List<oecp.framework.util.soap.ComplexType>  o = getDataStruct(strwsdl);
		System.out.println(o);
	}
	// 测试用
	private static String getFileContent(String filepath) throws Exception {
		String result = "";
		FileReader fr = null;
		BufferedReader br = null;
		try {
			File f = new File(filepath);
			f = new File(filepath);
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			String str = null;
			while((str = br.readLine())!=null){
				result = result.concat(str);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			try {
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				fr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 解析WSDL获得类型描述部分对数据结构的描述，并形成结构信息对象数组返回。
	 * 
	 * @author slx
	 * @date 2011-9-1上午09:45:42
	 * @param strwsdl
	 * @return
	 * @throws Exception
	 */
	public static List<oecp.framework.util.soap.ComplexType> getDataStruct(String strwsdl) throws Exception {
		Definition def = getWSDLDefinition(strwsdl);
		// 得到了schema 
		List<Schema> scheams = createSchema(def);
		// 分解schema
		List<oecp.framework.util.soap.ComplexType> t = getComplexTypes(scheams);
		return t;
	}
	
	/**
	 * 从WSDL的Schema对象列表中获得数据结构描述
	 * 
	 * @author slx
	 * @date 2011-9-1上午09:48:47
	 * @param schemas
	 * @return
	 */
	protected static List<oecp.framework.util.soap.ComplexType> getComplexTypes(List<Schema> schemas){
//		((ElementDecl)((Group)((ComplexType)scheams.get(0).getComplexTypes().nextElement()).enumerate().nextElement()).enumerate().nextElement()).getType().isComplexType()
		List<oecp.framework.util.soap.ComplexType> types = new ArrayList<oecp.framework.util.soap.ComplexType>();
		for (Schema schema : schemas) {
			Enumeration enumTypes = schema.getComplexTypes();
			while(enumTypes.hasMoreElements()){
				ComplexType type = (ComplexType)enumTypes.nextElement();
				oecp.framework.util.soap.ComplexType oecptype = transferComplexType(type);
				types.add(oecptype);
			}
		}
		
		return types;
	}
	
	/**
	 * 将一个org.exolab.castor.xml.schema.ComplexType
	 * 转换为oecp.framework.util.soap.ComplexType
	 * 
	 * @author slx
	 * @date 2011-9-1上午09:49:21
	 * @param type
	 * @return
	 */
	protected static oecp.framework.util.soap.ComplexType transferComplexType(ComplexType type){
		// 取出属性 递归内部复杂类型，形成对象。
//		((ElementDecl)((Group)((ComplexType)scheams.get(0).getComplexTypes().nextElement()).enumerate().nextElement()).enumerate().nextElement()).getType().isComplexType()
		// XXX 创建复杂杂型时每次都是new出来的。内部复杂类型有些应该与外部的指向同一个对象。算法待优化。
		oecp.framework.util.soap.ComplexType ctype = new oecp.framework.util.soap.ComplexType();
		ctype.setName(type.getName());
		ctype.setAttrs(new ArrayList<Attribute>());
		Enumeration groups = type.enumerate();
		while(groups.hasMoreElements()){
			Group group = (Group)groups.nextElement();
			Enumeration attrs = group.enumerate();
			while(attrs.hasMoreElements()){
				ElementDecl decl = (ElementDecl)attrs.nextElement();
				Attribute attr = transferElementDecl(decl);
				ctype.getAttrs().add(attr);
			}
		}
		
		return ctype;
	}
	/**
	 * 将WSDL中类型的属性ElementDecl描述转换为Attribute
	 * 
	 * @author slx
	 * @date 2011-9-1上午09:50:27
	 * @param decl
	 * @return
	 */
	protected static Attribute transferElementDecl(ElementDecl decl){
		Attribute attr = new Attribute();
		attr.setName(decl.getName());
		attr.setTypename(decl.getType().getName());
		attr.setMinOccurs(decl.getMinOccurs());
		attr.setMaxOccurs(decl.getMaxOccurs());
		attr.setNullable(decl.isNillable());
		attr.setComplextype(decl.getType().isComplexType());
		
		if(decl.getType().isComplexType()){
			attr.setType(transferComplexType((ComplexType)decl.getType()));
		}
		return attr;
	}
	/**
	 * 从WSDL字符传中得到Definition对象
	 * 
	 * @author slx
	 * @date 2011-9-1上午09:51:25
	 * @param strwsdl
	 * @return
	 * @throws Exception
	 */
	protected static Definition getWSDLDefinition(String strwsdl) throws Exception{
		StringReader strReader = new StringReader(strwsdl);
		InputSource is = new InputSource(strReader);
		WSDLReader wsdlReader = WSDLFactory.newInstance().newWSDLReader();
		Definition def = wsdlReader.readWSDL(null, is);
		return def;
	}

	/**
	 * 从Definition列表中获得Schema列表
	 * 
	 * @author slx
	 * @date 2011-9-1上午09:52:21
	 * @param wsdlDefinition
	 * @return
	 * @throws Exception
	 */
	protected static List<Schema> createSchema(Definition wsdlDefinition) throws Exception {
		List<Schema> schemas = new ArrayList<Schema>();
		org.w3c.dom.Element schemaElementt = null;
		if (wsdlDefinition.getTypes() != null) {
			List<ExtensibilityElement> schemaExtElem = findExtensibilityElement(wsdlDefinition.getTypes().getExtensibilityElements(), "schema");
			for (int i = 0; i < schemaExtElem.size(); i++) {
				ExtensibilityElement schemaElement = (ExtensibilityElement) schemaExtElem.get(i);
				if (schemaElement != null && schemaElement instanceof SchemaImpl) {
					schemaElementt = ((SchemaImpl) schemaElement).getElement();
					Schema schema = createschemafromtype(schemaElementt, wsdlDefinition);
					schemas.add(schema);
				}
			}

		}
		return schemas;
	}

	protected static List<ExtensibilityElement> findExtensibilityElement(List extensibilityElements, String elementType) {
		int i = 0;
		List<ExtensibilityElement> elements = new ArrayList<ExtensibilityElement>();
		if (extensibilityElements != null) {
			Iterator iter = extensibilityElements.iterator();
			while (iter.hasNext()) {
				ExtensibilityElement elment = (ExtensibilityElement) iter.next();
				if (elment.getElementType().getLocalPart().equalsIgnoreCase(elementType)) {
					elements.add(elment);
				}
			}
		}
		return elements;
	}

	protected static Schema createschemafromtype(org.w3c.dom.Element schemaElement, Definition wsdlDefinition) throws Exception {
		if (schemaElement == null) {
			return null;
		}
		DOMBuilder domBuilder = new DOMBuilder();
		org.jdom.Element jdomSchemaElement = domBuilder.build(schemaElement);
		if (jdomSchemaElement == null) {
			return null;
		}
		Map namespaces = wsdlDefinition.getNamespaces();
		if (namespaces != null && !namespaces.isEmpty()) {
			Iterator nsIter = namespaces.keySet().iterator();
			while (nsIter.hasNext()) {
				String nsPrefix = (String) nsIter.next();
				String nsURI = (String) namespaces.get(nsPrefix);
				if (nsPrefix != null && nsPrefix.length() > 0) {
					org.jdom.Namespace nsDecl = org.jdom.Namespace.getNamespace(nsPrefix, nsURI);
					jdomSchemaElement.addNamespaceDeclaration(nsDecl);
				}
			}
		}
		jdomSchemaElement.detach();
		Schema schema = convertElementToSchema(jdomSchemaElement);
		return schema;
	}

	protected static Schema convertElementToSchema(Element element) throws IOException {
		XMLOutputter xmlWriter = new XMLOutputter();
		String content = xmlWriter.outputString(element);
		if (content != null) {
			return readSchema(new StringReader(content));
		}
		return null;
	}

	protected static Schema readSchema(Reader reader) throws IOException {
		InputSource inputSource = new InputSource(reader);
		SchemaReader schemaReader = new SchemaReader(inputSource);
		schemaReader.setValidation(false);
		Schema schema = schemaReader.read();
		return schema;
	}
}
