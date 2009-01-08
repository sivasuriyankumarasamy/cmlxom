package org.xmlcml.cml.element;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import nu.xom.Node;

import org.xmlcml.cml.base.CMLConstants;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLBasisSet.Basis;
import org.xmlcml.molutil.ChemicalElement;

/**
 * user-modifiable class supporting atomicBasisFunction. * autogenerated from
 * schema use as a shell which can be edited
 *
 */
public class CMLAtomicBasisFunction extends AbstractAtomicBasisFunction {

	/** namespaced element name.*/
	public final static String NS = C_E+TAG;
    /**
     * constructor.
     */
    public CMLAtomicBasisFunction() {
    }

    /**
     * constructor.
     *
     * @param old
     */
    public CMLAtomicBasisFunction(CMLAtomicBasisFunction old) {
        super((AbstractAtomicBasisFunction) old);

    }

    /**
     * copy node .
     *
     * @return Node
     */
    public Node copy() {
        return new CMLAtomicBasisFunction(this);

    }

    /**
     * create new instance in context of parent, overridable by subclasses.
     *
     * @param parent
     *            parent of element to be constructed (ignored by default)
     * @return CMLAtomicBasisFunction
     */
    public CMLElement makeElementInContext(Element parent) {
        return new CMLAtomicBasisFunction();

    }

    /**
     * create from components.
     *
     * @param l
     *            quantum
     * @param m
     *            quantum
     * @param n
     *            quantum
     * @param lm
     * @param symbol
     * @param atomRef
     */
    public CMLAtomicBasisFunction(int l, int m, int n, String lm,
            String symbol, String atomRef) {
        this();
        this.setL(l);
        this.setM(m);
        this.setN(n);
        this.setLm(lm);
        this.setSymbol(symbol);
        this.setAtomRef(atomRef);
    }

    /**
     * get ordered list of ABFs for atom. uses basis and element to generate
     * list in order
     *
     * @param atom
     * @param basis
     * @return atomicBasisFuctions
     */
    public static List<CMLAtomicBasisFunction> getABFList(CMLAtom atom,
            Basis basis) {
        List<CMLAtomicBasisFunction> abfList = new ArrayList<CMLAtomicBasisFunction>();
        if (atom != null && atom.getId() != null) {
            ChemicalElement element = ChemicalElement.getChemicalElement(atom
                    .getElementType());
            if (element != null) {
                if (basis.equals(Basis.MINIMAL)) {
                    int n = element.getPeriod();
                    int m = 0;
                    int l = 0;
                    String id = atom.getId();
                    abfList.add(new CMLAtomicBasisFunction(l, m, n, "s", "s",
                            id));
                    if (n > 1) {
                        abfList.add(new CMLAtomicBasisFunction(l, m, n, "px",
                                "px", id));
                        m = -1;
                        abfList.add(new CMLAtomicBasisFunction(l, m, n, "py",
                                "py", id));
                        m = 1;
                        abfList.add(new CMLAtomicBasisFunction(l, m, n, "pz",
                                "pz", id));
                    }
                } else {
                    //
                }
            }
        }
        return abfList;
    }

    /**
     * simple string representation. concatenates n, lm, symbol and atom,
     * example 2s(s'), 3dx2-y2(DX2Y2)
     *
     * @return string
     */
    public String getString() {
        String s = CMLConstants.S_EMPTY + getN() + getLm() + CMLConstants.S_LBRAK + getSymbol() + ")(" + getId()
                + CMLConstants.S_RBRAK;
        return s;
    }
}
