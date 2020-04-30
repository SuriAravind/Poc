/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sf.cb2xml.sablecc.node;

import java.util.*;
import net.sf.cb2xml.sablecc.analysis.*;

public final class ASequenceLiteralSequence extends PLiteralSequence
{
    private PLiteralSequence _literalSequence_;
    private TComma _comma_;
    private PLiteral _literal_;

    public ASequenceLiteralSequence()
    {
    }

    public ASequenceLiteralSequence(
        PLiteralSequence _literalSequence_,
        TComma _comma_,
        PLiteral _literal_)
    {
        setLiteralSequence(_literalSequence_);

        setComma(_comma_);

        setLiteral(_literal_);

    }
    public Object clone()
    {
        return new ASequenceLiteralSequence(
            (PLiteralSequence) cloneNode(_literalSequence_),
            (TComma) cloneNode(_comma_),
            (PLiteral) cloneNode(_literal_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASequenceLiteralSequence(this);
    }

    public PLiteralSequence getLiteralSequence()
    {
        return _literalSequence_;
    }

    public void setLiteralSequence(PLiteralSequence node)
    {
        if(_literalSequence_ != null)
        {
            _literalSequence_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _literalSequence_ = node;
    }

    public TComma getComma()
    {
        return _comma_;
    }

    public void setComma(TComma node)
    {
        if(_comma_ != null)
        {
            _comma_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _comma_ = node;
    }

    public PLiteral getLiteral()
    {
        return _literal_;
    }

    public void setLiteral(PLiteral node)
    {
        if(_literal_ != null)
        {
            _literal_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _literal_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_literalSequence_)
            + toString(_comma_)
            + toString(_literal_);
    }

    void removeChild(Node child)
    {
        if(_literalSequence_ == child)
        {
            _literalSequence_ = null;
            return;
        }

        if(_comma_ == child)
        {
            _comma_ = null;
            return;
        }

        if(_literal_ == child)
        {
            _literal_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_literalSequence_ == oldChild)
        {
            setLiteralSequence((PLiteralSequence) newChild);
            return;
        }

        if(_comma_ == oldChild)
        {
            setComma((TComma) newChild);
            return;
        }

        if(_literal_ == oldChild)
        {
            setLiteral((PLiteral) newChild);
            return;
        }

    }
}
