/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sf.cb2xml.sablecc.node;

import java.util.*;
import net.sf.cb2xml.sablecc.analysis.*;

public final class AItem extends PItem
{
    private TNumberNot88 _numberNot88_;
    private PDataNameOrFiller _dataNameOrFiller_;
    private PRedefinesClause _redefinesClause_;
    private PClauseSequence _clauseSequence_;

    public AItem()
    {
    }

    public AItem(
        TNumberNot88 _numberNot88_,
        PDataNameOrFiller _dataNameOrFiller_,
        PRedefinesClause _redefinesClause_,
        PClauseSequence _clauseSequence_)
    {
        setNumberNot88(_numberNot88_);

        setDataNameOrFiller(_dataNameOrFiller_);

        setRedefinesClause(_redefinesClause_);

        setClauseSequence(_clauseSequence_);

    }
    public Object clone()
    {
        return new AItem(
            (TNumberNot88) cloneNode(_numberNot88_),
            (PDataNameOrFiller) cloneNode(_dataNameOrFiller_),
            (PRedefinesClause) cloneNode(_redefinesClause_),
            (PClauseSequence) cloneNode(_clauseSequence_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAItem(this);
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

    public PDataNameOrFiller getDataNameOrFiller()
    {
        return _dataNameOrFiller_;
    }

    public void setDataNameOrFiller(PDataNameOrFiller node)
    {
        if(_dataNameOrFiller_ != null)
        {
            _dataNameOrFiller_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _dataNameOrFiller_ = node;
    }

    public PRedefinesClause getRedefinesClause()
    {
        return _redefinesClause_;
    }

    public void setRedefinesClause(PRedefinesClause node)
    {
        if(_redefinesClause_ != null)
        {
            _redefinesClause_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _redefinesClause_ = node;
    }

    public PClauseSequence getClauseSequence()
    {
        return _clauseSequence_;
    }

    public void setClauseSequence(PClauseSequence node)
    {
        if(_clauseSequence_ != null)
        {
            _clauseSequence_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _clauseSequence_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_numberNot88_)
            + toString(_dataNameOrFiller_)
            + toString(_redefinesClause_)
            + toString(_clauseSequence_);
    }

    void removeChild(Node child)
    {
        if(_numberNot88_ == child)
        {
            _numberNot88_ = null;
            return;
        }

        if(_dataNameOrFiller_ == child)
        {
            _dataNameOrFiller_ = null;
            return;
        }

        if(_redefinesClause_ == child)
        {
            _redefinesClause_ = null;
            return;
        }

        if(_clauseSequence_ == child)
        {
            _clauseSequence_ = null;
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

        if(_dataNameOrFiller_ == oldChild)
        {
            setDataNameOrFiller((PDataNameOrFiller) newChild);
            return;
        }

        if(_redefinesClause_ == oldChild)
        {
            setRedefinesClause((PRedefinesClause) newChild);
            return;
        }

        if(_clauseSequence_ == oldChild)
        {
            setClauseSequence((PClauseSequence) newChild);
            return;
        }

    }
}
