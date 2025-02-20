package za.vutshilalabs.bpelgen.popup.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eventb.core.IMachineRoot;

import za.vutshilalabs.bpelgen.core.translation.Translator;

public class GenerateAction implements IObjectActionDelegate {

	/**
	 * Constructor for Action1.
	 */
	public GenerateAction() {
		super();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		ISelection selection = window.getSelectionService().getSelection();

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			Object obj = ss.getFirstElement();

			// handle Event-B machine file from Event-B perspective
			if (obj instanceof IMachineRoot) {
				IMachineRoot machine = (IMachineRoot) obj;
				Translator.translateEventb(machine);
			}

			// handle Event-B machine file out of Event-B perspective
			// i.e. .bum .bcm files

			else if (obj instanceof IFile) {

				IFile machineFile = (IFile) obj;
				Translator.translateEventb(machineFile);
			}

		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		targetPart.getSite().getShell();
	}

}
