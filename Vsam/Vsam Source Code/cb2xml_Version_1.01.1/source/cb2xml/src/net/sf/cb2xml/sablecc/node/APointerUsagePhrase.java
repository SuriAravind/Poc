/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sf.cb2xml.sablecc.node;

import net.sf.cb2xml.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class APointerUsagePhrase extends PUsagePhrase
{
    private TPointer _pointer_;

    public APointerUsagePhrase()
    {
        // Constructor
    }

    public APointerUsagePhrase(
        @SuppressWarnings("hiding") TPointer _pointer_)
    {
        // Constructor
        setPointer(_pointer_);

    }

    @Override
    public Object clone()
    {
        return new APointerUsagePhrase(
            cloneNode(this._pointer_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAPointerUsagePhrase(this);
    }

    public TPointer getPointer()
    {
        return this._pointer_;
    }

    public void setPointer(TPointer node)
    {
        if(this._pointer_ != null)
        {
            this._pointer_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._pointer_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._pointer_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._pointer_ == child)
        {
            this._pointer_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._pointer_ == oldChild)
        {
            setPointer((TPointer) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}