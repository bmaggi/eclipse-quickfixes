package quickfix;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

/**
 * Provide the list of specific solutions for problems
 * 
 * Use case: Replace
 * StereotypeApplicationHelper.getInstance(null).applyStereotype(validViewpoint,
 * ModelelementsPackage.eINSTANCE.getViewpoint()); By
 * StereotypeApplicationHelper.getInstance(null).applyStereotype(validViewpoint,
 * ModelelementsPackage.eINSTANCE.getViewpoint(),null);
 */
public class DeprecatedMarkerResolutionGenerator implements IMarkerResolutionGenerator {

	public IMarkerResolution[] getResolutions(IMarker mk) {
		try {
			if ("The method applyStereotype(Element, EClass) from the type UMLUtil.StereotypeApplicationHelper is deprecated"
					.equals(mk.getAttribute("message"))) {
				return new IMarkerResolution[] { new AddNullParamMarkerResolution() };
			}
		} catch (CoreException e) {
		}
		return new IMarkerResolution[0];
	}
}