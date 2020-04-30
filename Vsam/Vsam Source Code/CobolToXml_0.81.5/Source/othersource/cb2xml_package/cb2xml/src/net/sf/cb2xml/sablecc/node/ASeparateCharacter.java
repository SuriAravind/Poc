/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sf.cb2xml.sablecc.node;

import java.util.*;
import net.sf.cb2xml.sablecc.analysis.*;

public final class ASeparateCharacter extends PSeparateCharacter
{
    private TSeparate _separate_;
    private TCharacter _character_;

    public ASeparateCharacter()
    {
    }

    public ASeparateCharacter(
        TSeparate _separate_,
        TCharacter _character_)
    {
        setSeparate(_separate_);

        setCharacter(_character_);

    }
    public Object clone()
    {
        return new ASeparateCharacter(
            (TSeparate) cloneNode(_separate_),
            (TCharacter) cloneNode(_character_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASeparateCharacter(this);
    }

    public TSeparate getSeparate()
    {
        return _separate_;
    }

    public void setSeparate(TSeparate node)
    {
        if(_separate_ != null)
        {
            _separate_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _separate_ = node;
    }

    public TCharacter getCharacter()
    {
        return _character_;
    }

    public void setCharacter(TCharacter node)
    {
        if(_character_ != null)
        {
            _character_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _character_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_separate_)
            + toString(_character_);
    }

    void removeChild(Node child)
    {
        if(_separate_ == child)
        {
            _separate_ = null;
            return;
        }

        if(_character_ == child)
        {
            _character_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_separate_ == oldChild)
        {
            setSeparate((TSeparate) newChild);
            return;
        }

        if(_character_ == oldChild)
        {
            setCharacter((TCharacter) newChild);
            return;
        }

    }
}
