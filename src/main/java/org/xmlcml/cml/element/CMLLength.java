package org.xmlcml.cml.element;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

import org.xmlcml.cml.base.CMLConstants;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.base.CMLElements;
import org.xmlcml.euclid.Point3;
import org.xmlcml.euclid.Util;

/**
 * user-modifiable class supporting length. * autogenerated from schema use as a
 * shell which can be edited
 *
 */
public class CMLLength extends AbstractLength {

	/** namespaced element name.*/
	public final static String NS = C_E+TAG;

    /**
     * constructor.
     */
    public CMLLength() {
    }

    /**
     * constructor.
     *
     * @param old
     */
    public CMLLength(CMLLength old) {
        super((AbstractLength) old);

    }

    /**
     * copy node .
     *
     * @return Node
     */
    public Node copy() {
        return new CMLLength(this);

    }

    /**
     * create new instance in context of parent, overridable by subclasses.
     *
     * @param parent
     *            parent of element to be constructed (ignored by default)
     * @return CMLLength
     */
    public CMLElement makeElementInContext(Element parent) {
        return new CMLLength();

    }

    /**
     * gets atomIds as list.
     *
     * @return the atomIds (null if no atomRefs3)
     */
    public List<String> getAtomIds() {
        List<String> idList = null;
        String[] atomRefs2 = getAtomRefs2();
        if (atomRefs2 != null) {
            idList = new ArrayList<String>();
            for (String s : atomRefs2) {
                idList.add(s);
            }
        }
        return idList;
    }

    /**
     * gets atomRefs3 as array of atoms.
     *
     * uses the value in <angle> element
     * @param molecule
     * @return the atoms (null if no atomRefs3)
     */
    public List<CMLAtom> getAtoms(CMLMolecule molecule) {
        List<CMLAtom> atomList = null;
        if (molecule != null) {
            String[] atomRefs2 = this.getAtomRefs2();
            if (atomRefs2 != null) {
                atomList = new ArrayList<CMLAtom>();
                for (String atomRef : atomRefs2) {
                    CMLAtom atom = molecule.getAtomById(atomRef);
                    if (atom == null) {
                        throw new RuntimeException("cannot find atom " + atomRef);
                    }
                    atomList.add(molecule.getAtomById(atomRef));
                }
            }
        }
        return atomList;
    }

    /**
     * gets value calculated from coordinates. does not set content
     *
     * @param molecule
     *
     * @return the length (NaN if cannot calculate)
     */
    public double getCalculatedLength(CMLMolecule molecule) {
        double calculatedLength = Double.NaN;
        if (molecule == null) {
            throw new RuntimeException("length requires molecule");
        }
        List<CMLAtom> atoms = getAtoms(molecule);
        List<Point3> coordList = new ArrayList<Point3>();
        if (atoms != null) {
            for (CMLAtom atom : atoms) {
                Point3 point = atom.getXYZ3();
                if (point != null) {
                    coordList.add(point);
                }
            }
            if (coordList.size() == 2) {
                calculatedLength = coordList.get(0).getDistanceFromPoint(coordList.get(1));
            }
        } else {
            throw new RuntimeException("length requires atomRefs2");
        }
        return calculatedLength;
    }

    /** translates elements to list.
     * @param lengthElements
     * @return the list of lengths
     * @deprecated try to use List<CMLLength>
     */
    public static List<CMLLength> getList(CMLElements<CMLLength> lengthElements) {
        List<CMLLength> lengthList = new ArrayList<CMLLength>();
        for (CMLLength length : lengthElements) {
            lengthList.add(length);
        }
        return lengthList;
    }

    /** add ids into list of length atomRefs2.
     *
     * @param lengthList lists
     * @param atom0
     * @param atom1
     */
    public static void addAtomIds(
            List<CMLLength>lengthList, CMLAtom atom0, CMLAtom atom1
        ) {

        // add explicit atom ids into lengths
        for (CMLLength length : lengthList) {
            length.setAtomRefs2(atom0, atom1);
        }
    }

    /**
     * get lengths indexed by bond hash.
     *
     * @param lengths
     * @return map
     */
    public static Map<String, CMLLength> getIndexedLengths(List<CMLLength> lengths) {
        Map<String, CMLLength> lengthTable = new HashMap<String, CMLLength>();
        for (CMLLength length : lengths) {
            String[] id = length.getAtomRefs2();
            String key = CMLBond.atomHash(id[0], id[1]);
            lengthTable.put(key, length);
        }
        return lengthTable;
    }

    /** add ids to atomRefs2
     *
     * @param atom0
     * @param atom1
     */
    public void setAtomRefs2(CMLAtom atom0, CMLAtom atom1) {
        this.setAtomRefs2(new String[]{atom0.getId(), atom1.getId()});
    }

    /** create key from atomRefs2 attribute using CMLBond atomHash.
     *
     * @return the hash null if no atomRefs2
     */
    public String atomHash() {
        String[] a = this.getAtomRefs2();
        return (a == null) ? null : CMLBond.atomHash(a[0], a[1]);
    }

    /** get string.
     *
     * @return the string
     */
    public String getString() {
        String s = CMLConstants.S_EMPTY;
        String[] a = getAtomRefs2();
        if (a != null) {
            s += Util.concatenate(a, CMLConstants.S_MINUS);
        }
        s += CMLConstants.S_SPACE;
        s += this.getXMLContent();
        return s;
    }

    /** writes lengths as an XHTML table.
     * columns are atom1.label atom2.label distance in A
     * @param w writer to output
     * @param lengthList
     * @param molecule
     * @throws IOException
     */
    public static void outputHTML(
        Writer w, List<CMLLength> lengthList,
        CMLMolecule molecule) throws IOException {
        if (lengthList.size() > 0) {
            w.write("<table border='1'>\n");
            w.write("<tr>");
            w.write("<th>");
            w.write("atom1 (id)");
            w.write("</th>");
            w.write("<th>");
            w.write("atom2 (id)");
            w.write("</th>");
            w.write("<th>");
            w.write("length");
            w.write("</th>");
            w.write("</tr>\n");
            for (CMLLength length : lengthList) {
                List<CMLAtom> atoms = length.getAtoms(molecule);
                w.write("<tr>");
                for (int i = 0; i < 2; i++) {
                    w.write("<td>");
                    CMLAtom atom = atoms.get(i);
                    Nodes labelNodes = atom.query(
                        CMLScalar.NS+"[@dictRef='iucr:_atom_site_label']", CMLConstants.CML_XPATH);
                    String label = ((CMLScalar) labelNodes.get(0)).getXMLContent()+" ("+atom.getId()+S_RBRAK;
                    w.write( (label == null) ? atom.getId() : label);
                    w.write("</td>");
                }
                String s = ""+length.getXMLContent();
                w.write("<td>"+s.substring(0, Math.min(6, s.length()))+"</td>");
                w.write("</tr>\n");
            }
            w.write("</table>\n");
        }
    }


}
