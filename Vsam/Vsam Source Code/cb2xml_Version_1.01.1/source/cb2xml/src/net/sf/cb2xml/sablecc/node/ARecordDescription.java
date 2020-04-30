/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sf.cb2xml.sablecc.node;

import net.sf.cb2xml.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ARecordDescription extends PRecordDescription
{
    private PGroupItem _groupItem_;
    private TDot _dot_;

    public ARecordDescription()
    {
        // Constructor
    }

    public ARecordDescription(
        @SuppressWarnings("hiding") PGroupItem _groupItem_,
        @SuppressWarnings("hiding") TDot _dot_)
    {
        // Constructor
        setGroupItem(_groupItem_);

        setDot(_dot_);

    }

    @Override
    public Object clone()
    {
        return new ARecordDescription(
            cloneNode(this._groupItem_),
            cloneNode(this._dot_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseARecordDescription(this);
    }

    public PGroupItem getGroupItem()
    {
        return this._groupItem_;
    }

    public void setGroupItem(PGroupItem node)
    {
        if(this._groupItem_ != null)
        {
            this._groupItem_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._groupItem_ = node;
    }

    public TDot getDot()
    {
        return this._dot_;
    }

    public void setDot(TDot node)
    {
        if(this._dot_ != null)
        {
            this._dot_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._dot_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._groupItem_)
            + toString(this._dot_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._groupItem_ == child)
        {
            this._groupItem_ = null;
            return;
        }

        if(this._dot_ == child)
        {
            this._dot_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._groupItem_ == oldChild)
        {
            setGroupItem((PGroupItem) newChild);
            return;
        }

        if(this._dot_ == oldChild)
        {
            setDot((TDot) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
