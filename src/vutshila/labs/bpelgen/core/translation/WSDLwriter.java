package vutshila.labs.bpelgen.core.translation;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eventb.core.IAxiom;
import org.eventb.core.IContextRoot;
import org.eventb.core.IMachineRoot;
import org.eventb.core.ISCContextRoot;
import org.rodinp.core.RodinDBException;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import vutshila.labs.bpelgen.BpelgenPlugin;
import vutshila.labs.bpelgen.core.XMLtool;

/**
 * Create WSDL file from Event-B context
 * 
 * @author Mashele Ernest <mashern@tuks.co.za>
 * 
 */
public class WSDLwriter {

    private IContextRoot context;
    private Document document;
    private Element definitions;

    public WSDLwriter(IMachineRoot machine) {
	init(machine);
    }

    /**
     * Initialise the components of WSDL file
     */
    public void init(IMachineRoot machine) {
	// XXX change this to use native contexct file
	ISCContextRoot sCContext = machine.getSCContextRoot();
	context = sCContext.getContextRoot();
	System.out.println(context.getComponentName());
	XMLtool xml = new XMLtool(true, null);
	document = xml.getDocument();

	Comment generated = document
		.createComment("Generated by BPEL Generator plugin");
	document.appendChild(generated);

	createDefinitions(machine.getComponentName().concat("Service"));
	createTypes();
	createMessages();
	createPorttypes();
	createBindings();
	createService();
	System.out.println("Testing 123");

    }

    /**
     * Create the top-level WSDL element
     * 
     * @param name
     *            - Service name
     */
    private void createDefinitions(String name) {
	definitions = document.createElement("definitions");
	definitions.setAttribute("name", name);
	definitions.setAttribute("targetNamespace", "http://localhost");
	definitions.setAttribute("xmlns", "http://schemas.xmlsoap.org/wsdl/");
	definitions.setAttribute("xmlns:soap",
		"http://schemas.xmlsoap.org/wsdl/soap/");
	definitions.setAttribute("xmlns:xsd",
		"http://www.w3.org/2001/XMLSchema");
	Comment stub = document
		.createComment("TODO Auto-generated targetNamespace stub");
	document.appendChild(stub);
	document.appendChild(definitions);
    }

    /**
     * Create WSDL service
     */
    private void createService() {
	Element service = document.createElement("service");
	Comment stub = document
		.createComment("TODO Auto-generated service stub");
	service.appendChild(stub);
	definitions.appendChild(service);
    }

    /**
     * Create supported types
     */
    private void createTypes() {

	Element types = document.createElement("types");
	definitions.appendChild(types);
    }

    /**
     * Create WSDL binding element
     */
    private void createBindings() {
	Element binding = document.createElement("binding");
	Comment stub = document
		.createComment("TODO Auto-generated binding stub");
	binding.appendChild(stub);
	definitions.appendChild(binding);
    }

    /**
     * Create WSDL portTypes
     */
    private void createPorttypes() {
	Element portType = document.createElement("portType");
	definitions.appendChild(portType);
    }

    /**
     * Create WSDL messages
     */
    private void createMessages() {
	IAxiom[] axioms;
	// HashTable<PredicateString> predicates = new
	// HashTable<PredicateString>();
	try {
	    axioms = context.getAxioms();

	    for (IAxiom axiom : axioms) {
		System.out.println(axiom.getLabel());
		System.out.println(axiom.getPredicateString());
		// PredicateString ps = new PredicateString();
		// ps.createPredicate(predicate)
	    }
	} catch (RodinDBException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * Write WSDL data to file
     * 
     * @param file
     */
    public void createFile(IFile file) {
	TransformerFactory factory;
	Source source;
	Transformer transformer;
	Result result;
	try {
	    factory = TransformerFactory.newInstance();
	    transformer = factory.newTransformer();
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    source = new DOMSource(document);
	    result = new StreamResult(file.getLocation().toFile());
	    transformer.transform(source, result);
	    file.refreshLocal(0, null);

	} catch (TransformerConfigurationException e) {
	    BpelgenPlugin.logError(e, e.getMessage());
	} catch (TransformerException e) {
	    BpelgenPlugin.logError(e, e.getMessage());
	} catch (CoreException e) {
	    e.printStackTrace();
	}

    }
}
