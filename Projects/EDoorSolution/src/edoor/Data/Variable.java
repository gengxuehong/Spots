/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.Data;

/**
 * Wrapper class for enclose Object that acts like OLE variable
 * @author Geng Xuehong
 */
public class Variable {
    private Object _val = null;
    
    public Variable() {
    }
    public Variable(Object val) {
        _val = val;
    }
    public Variable(Variable other) {
        _val = other._val;
    }
    public Variable(int i) {
        _val = Integer.valueOf(i);
    }
    public Variable(short s) {
        _val = Short.valueOf(s);
    }
    public Variable(long l) {
        _val = Long.valueOf(l);
    }
    public Variable(boolean b) {
        _val = Boolean.valueOf(b);
    }
    public Variable(float f) {
        _val = Float.valueOf(f);
    }
    public Variable(double f) {
        _val = Double.valueOf(f);
    }
    public Variable(byte b) {
        _val = Byte.valueOf(b);
    }
    public Variable(char c) {
        _val = Character.valueOf(c);
    }
    
    @Override
    public String toString() {
        return _val.toString();
    }
    public int toInteger() {
        return Integer.valueOf(toString());
    }
    public short toShort() {
        return Short.valueOf(toString());
    }
    public long toLong() {
        return Long.valueOf(toString());
    }
    public boolean toBoolean() {
        return Boolean.valueOf(toString());
    }
    public float toFloat() {
        return Float.valueOf(toString());
    }
    public double toDouble() {
        return Double.valueOf(toString());
    }
    public byte toByte() {
        return Byte.valueOf(toString());
    }
    public char toChar() {
        return (char)toByte();
    }
    public java.sql.Date toDate() {
        return java.sql.Date.valueOf(toString());
    }
    public java.sql.Time toTime() {
        return java.sql.Time.valueOf(toString());
    }
    public java.sql.Timestamp toTimestamp() {
        return java.sql.Timestamp.valueOf(toString());
    }
    public java.math.BigDecimal toBigDecimal() {
        return java.math.BigDecimal.valueOf(toDouble());
    }
    public java.math.BigInteger toBigInteger() {
        return java.math.BigInteger.valueOf(toLong());
    }
    
    public Object ConvertTo(Class<?> toClass) {
        if(toClass.isAssignableFrom(String.class))
            return toString();
        else if(toClass.isAssignableFrom(int.class))
            return toInteger();
        else if(toClass.isAssignableFrom(short.class))
            return toShort();
        else if(toClass.isAssignableFrom(long.class))
            return toLong();
        else if(toClass.isAssignableFrom(boolean.class))
            return toBoolean();
        else if(toClass.isAssignableFrom(float.class))
            return toFloat();
        else if(toClass.isAssignableFrom(double.class))
            return toDouble();
        else if(toClass.isAssignableFrom(byte.class))
            return toByte();
        else if(toClass.isAssignableFrom(char.class))
            return toChar();
        else if(toClass.isAssignableFrom(java.sql.Date.class))
            return toDate();
        else if(toClass.isAssignableFrom(java.sql.Time.class))
            return toTime();
        else if(toClass.isAssignableFrom(java.sql.Timestamp.class))
            return toTimestamp();
        else if(toClass.isAssignableFrom(java.math.BigDecimal.class))
            return toBigDecimal();
        else if(toClass.isAssignableFrom(java.math.BigInteger.class))
            return toBigInteger();
        else
            return null;
    }
}
