/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sf.cb2xml.sablecc.node;

import net.sf.cb2xml.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ANumber88Number extends PNumber
{
    private TNumber88 _number88_;

    public ANumber88Number()
    {
        // Constructor
    }

    public ANumber88Number(
        @SuppressWarnings("hiding") TNumber88 _number88_)
    {
        // Constructor
        setNumber88(_number88_);

    }

    @Override
    public Object clone()
    {
        return new ANumber88Number(
            cloneNode(this._number88_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseANumber88Number(this);
    }

    public TNumber88 getNumber88()
    {
        return this._number88_;
    }

    public void setNumber88(TNumber88 node)
    {
        if(this._number88_ != null)
        {
            this._number88_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._number88_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._number88_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._number88_ == child)
        {
            this._number88_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._number88_ == oldChild)
        {
            setNumber88((TNumber88) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
