/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sf.cb2xml.sablecc.node;

import net.sf.cb2xml.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ADotMinusCharacterSubstring extends PCharacterSubstring
{
    private TDotMinus _dotMinus_;

    public ADotMinusCharacterSubstring()
    {
        // Constructor
    }

    public ADotMinusCharacterSubstring(
        @SuppressWarnings("hiding") TDotMinus _dotMinus_)
    {
        // Constructor
        setDotMinus(_dotMinus_);

    }

    @Override
    public Object clone()
    {
        return new ADotMinusCharacterSubstring(
            cloneNode(this._dotMinus_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseADotMinusCharacterSubstring(this);
    }

    public TDotMinus getDotMinus()
    {
        return this._dotMinus_;
    }

    public void setDotMinus(TDotMinus node)
    {
        if(this._dotMinus_ != null)
        {
            this._dotMinus_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._dotMinus_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._dotMinus_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._dotMinus_ == child)
        {
            this._dotMinus_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._dotMinus_ == oldChild)
        {
            setDotMinus((TDotMinus) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
