/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sf.cb2xml.sablecc.node;

import net.sf.cb2xml.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AGlobalClause extends PGlobalClause
{
    private TGlobal _global_;

    public AGlobalClause()
    {
        // Constructor
    }

    public AGlobalClause(
        @SuppressWarnings("hiding") TGlobal _global_)
    {
        // Constructor
        setGlobal(_global_);

    }

    @Override
    public Object clone()
    {
        return new AGlobalClause(
            cloneNode(this._global_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAGlobalClause(this);
    }

    public TGlobal getGlobal()
    {
        return this._global_;
    }

    public void setGlobal(TGlobal node)
    {
        if(this._global_ != null)
        {
            this._global_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._global_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._global_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._global_ == child)
        {
            this._global_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._global_ == oldChild)
        {
            setGlobal((TGlobal) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
