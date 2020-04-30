/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sf.cb2xml.sablecc.node;

import java.util.*;
import net.sf.cb2xml.sablecc.analysis.*;

public final class APlusCharacterSubstring extends PCharacterSubstring
{
    private TPlus _plus_;

    public APlusCharacterSubstring()
    {
    }

    public APlusCharacterSubstring(
        TPlus _plus_)
    {
        setPlus(_plus_);

    }
    public Object clone()
    {
        return new APlusCharacterSubstring(
            (TPlus) cloneNode(_plus_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAPlusCharacterSubstring(this);
    }

    public TPlus getPlus()
    {
        return _plus_;
    }

    public void setPlus(TPlus node)
    {
        if(_plus_ != null)
        {
            _plus_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _plus_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_plus_);
    }

    void removeChild(Node child)
    {
        if(_plus_ == child)
        {
            _plus_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_plus_ == oldChild)
        {
            setPlus((TPlus) newChild);
            return;
        }

    }
}
