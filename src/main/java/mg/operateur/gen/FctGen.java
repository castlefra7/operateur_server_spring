/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.gen;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lacha
 */
public class FctGen {
    
    public static String getString(String req, Connection conn) throws SQLException {
        if(conn == null) throw new NullPointerException("La connection est nulle");
        String result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(req);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                result = rs.getString(0);
            }
        } catch(SQLException ex) {
            throw ex;
        } finally {
            if(pstmt!=null) pstmt.close();
            if(rs!=null) rs.close();
        }
        
        return result;
    }
    
    public static Date getDate(String req, String colName, Connection conn) throws SQLException {
        if(conn == null) throw new NullPointerException("La connection est nulle");
        Date result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            System.out.println(req);
            pstmt = conn.prepareStatement(req);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                result = rs.getTimestamp(colName);
            }
        } catch(SQLException ex) {
            throw ex;
        } finally {
            if(pstmt!=null) pstmt.close();
            if(rs!=null) rs.close();
        }
        
        return result;
    }
    
    public static double getAmount(String req, String colName, Connection conn) throws SQLException {
        if(conn == null) throw new NullPointerException("La connection est nulle");
        double result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(req);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                result = rs.getDouble(colName);
            }
        } catch(SQLException ex) {
            throw ex;
        } finally {
            if(pstmt!=null) pstmt.close();
            if(rs!=null) rs.close();
        }
        
        return result;
    }
    
    public static Object find(Object ob, String req, String[] columns, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object result = null;
        List<Object> all = findAll(ob, req, columns, conn);
        if(all.size() > 0) result = all.get(0);
        return result;
    }
    
    public static List<Object> findAll(Object ob, String req, String[] columns, Connection conn) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        if(conn == null) throw new NullPointerException("La connection est nulle");
        List<Object> result = new ArrayList();
        
        Class cls = ob.getClass();
        Class[] paramsCons = new Class[0];
        Constructor constructor = cls.getConstructor(paramsCons);
        
        List<Method> methods = getSpecMethods(cls.getMethods(), "set");
        List<Method> neededMethods = new ArrayList();
        
        for(String column: columns) {
            for(Method method: methods) {
                String methodName = method.getName().toLowerCase().substring(3);
                if(methodName.equals(column)) {
                    neededMethods.add(method);
                    break;
                }
            }
        }
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(req);
            rs = pstmt.executeQuery();
            Object[] params = new Object[0];
            while(rs.next()) {
                Object newInstance = constructor.newInstance(params);
                for(Method method: neededMethods) {
                    setValues(newInstance, method, rs);
                }
                result.add(newInstance);
            }
        } catch(SQLException ex) {
            throw ex;
        } finally {
            if(pstmt != null) pstmt.close();
            if(rs != null) rs.close();
        }
        
        return result;
    }
    
    public static void setValues(Object ob, Method method, ResultSet rs) throws NoSuchMethodException, SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if(method.getParameters().length <= 0) throw new NoSuchMethodException("Invalide setter");
        Class returnClass = method.getParameterTypes()[0];
        Object[] args = new Object[1]; 
        String colName = method.getName().toLowerCase().substring(3);
        if(Number.class.isAssignableFrom(returnClass) || returnClass.getSimpleName().equals("int") || returnClass.getSimpleName().equals("float") || returnClass.getSimpleName().equals("double")) {
            if(returnClass.getSimpleName().equals("int")) {
                args[0] = rs.getInt(colName);
            } else if (returnClass.getSimpleName().equals("double")) {
                args[0] = rs.getDouble(colName);
            } else if (returnClass.getSimpleName().equals("float")) {
                args[0] = rs.getFloat(colName);
            } else {
                throw new IllegalArgumentException("Type de données non gérer");
            }
        } else if (returnClass.getSimpleName().toLowerCase().equals("string")) {
            args[0] = rs.getString(colName);
        } else if (returnClass.getSimpleName().toLowerCase().equals("date")) {
            args[0] = rs.getDate(colName);
        }
        method.invoke(ob, args);
    }
    
    
    public static List<Method> getSpecMethods(Method[] methods, String prefix) {
        List<Method> result = new ArrayList<>();
        for(Method method: methods) {
            if(method.getName().startsWith(prefix)) {
                result.add(method);
            }
        }
        return result;
    }
    
    public static String methodReturnValue(Object ob, Method method) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String result = "";
        Class returnClass = method.getReturnType();
        Object[] params = new Object[0];
        Object value;
        value = method.invoke(ob, params);
        if(Number.class.isAssignableFrom(returnClass) || returnClass.getSimpleName().equals("int") || returnClass.getSimpleName().equals("float") || returnClass.getSimpleName().equals("double")) {
            if( value != null)result = String.valueOf(value);
        } else if (returnClass.getSimpleName().toLowerCase().equals("string")) {
             if( value != null)result = "'" + (String)value + "'";
        } else if (returnClass.getSimpleName().toLowerCase().equals("date")) {
            if (value != null) result = "'" + value.toString() + "'";
        }
        return result;
    }
    
    
    public static void insert(Object ob, String columns, String tableName, Connection conn) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
        insert(ob, columns.split(";"), tableName, conn);
    }
    
    public static void insert(Object ob, String[] columns, String tableName, Connection conn) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
        if(conn == null) throw new NullPointerException("La connection est nulle");
        Class cls = ob.getClass();
        List<Method> methods = getSpecMethods(cls.getMethods(), "get");
        
        String values = "(";
        String columnNames = "(";
        for(String columnName: columns) {
            columnName = columnName.toLowerCase();
            for(Method method: methods) {
                String methodName = method.getName().toLowerCase().substring(3);
                if(columnName.equals(methodName)) {
                    columnNames += columnName + ",";
                    method.getReturnType();
                    values += methodReturnValue(ob, method) + ",";
                    break;
                }
            }
        }
        values = values.substring(0, values.length()-1) + ")";
        columnNames = columnNames.substring(0, columnNames.length()-1) + ")";
        String req = String.format("insert into %s %s values %s", tableName, columnNames, values);
        System.out.println(req);
        PreparedStatement pstmt = null;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(req);
            pstmt.executeUpdate();
            conn.commit();
        } catch(SQLException ex) {
            throw ex;
        } finally {
            if(pstmt!=null) pstmt.close();
        }
    }
}
