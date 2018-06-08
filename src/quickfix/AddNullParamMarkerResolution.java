package quickfix;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.SimpleMarkerAnnotation;

/**
 * Inspired from EPL code
 * https://github.com/eclipse/eclipse.jdt.ui/blob/master/org.eclipse.jdt.ui.tests/ui/org/eclipse/jdt/ui/tests/quickfix/MarkerResolutionGenerator.java
 * 
 * Maybe a nicer solution here
 * https://github.com/acanda/eclipse-pmd/blob/master/ch.acanda.eclipse.pmd.java/src/ch/acanda/eclipse/pmd/java/resolution/ASTQuickFix.java#L152
 * 
 * Marker resolution that adds a null last parameter",null" 
 * My use case: replacing 
 */
public class AddNullParamMarkerResolution implements IMarkerResolution {

	public AddNullParamMarkerResolution() {
	}

	/* (non-Javadoc)
	 * @see IMarkerResolution#getLabel()
	 */
	public String getLabel() {
		return "Add null missing parameter";
	}

	/* (non-Javadoc)
	 * @see IMarkerResolution#run(IMarker)
	 */
	public void run(IMarker marker) {
		FileEditorInput input= new FileEditorInput((IFile) marker.getResource());
		IAnnotationModel model= JavaUI.getDocumentProvider().getAnnotationModel(input);
		if (model != null) {
			// resource is open in editor

			Position pos= findProblemPosition(model, marker);
			if (pos != null) {
				IDocument doc= JavaUI.getDocumentProvider().getDocument(input);
				try {
					String str= doc.get(pos.getOffset(), pos.getLength());
					doc.replace(pos.getOffset(), pos.getLength(), str.substring(0, str.length()-1) + ",null)");
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		} else {
			// resource is not open in editor
			// to do: work on the resource
		}
		try {
			marker.delete();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private Position findProblemPosition(IAnnotationModel model, IMarker marker) {
		Iterator iter= model.getAnnotationIterator();
		while (iter.hasNext()) {
			Object curr= iter.next();
			if (curr instanceof SimpleMarkerAnnotation) {
				SimpleMarkerAnnotation annot= (SimpleMarkerAnnotation) curr;
				if (marker.equals(annot.getMarker())) {
					return model.getPosition(annot);
				}
			}
		}
		return null;
	}
}