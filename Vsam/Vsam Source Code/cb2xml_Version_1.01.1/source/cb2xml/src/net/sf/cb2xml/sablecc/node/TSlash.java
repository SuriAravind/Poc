/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sf.cb2xml.sablecc.node;

import net.sf.cb2xml.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TSlash extends Token
{
    public TSlash()
    {
        super.setText("/");
    }

    public TSlash(int line, int pos)
    {
        super.setText("/");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TSlash(getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTSlash(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TSlash text.");
    }
}
