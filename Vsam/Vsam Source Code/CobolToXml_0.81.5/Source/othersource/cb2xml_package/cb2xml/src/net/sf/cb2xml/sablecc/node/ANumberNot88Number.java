/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sf.cb2xml.sablecc.node;

import java.util.*;
import net.sf.cb2xml.sablecc.analysis.*;

public final class ANumberNot88Number extends PNumber
{
    private TNumberNot88 _numberNot88_;

    public ANumberNot88Number()
    {
    }

    public ANumberNot88Number(
        TNumberNot88 _numberNot88_)
    {
        setNumberNot88(_numberNot88_);

    }
    public Object clone()
    {
        return new ANumberNot88Number(
            (TNumberNot88) cloneNode(_numberNot88_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseANumberNot88Number(this);
    }

    public TNumberNot88 getNumberNot88()
    {
        return _numberNot88_;
    }

    public void setNumberNot88(TNumberNot88 node)
    {
        if(_numberNot88_ != null)
        {
            _numberNot88_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _numberNot88_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_numberNot88_);
    }

    void removeChild(Node child)
    {
        if(_numberNot88_ == child)
        {
            _numberNot88_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_numberNot88_ == oldChild)
        {
            setNumberNot88((TNumberNot88) newChild);
            return;
        }

    }
}