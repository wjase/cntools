
package com.cybernostics.lib.io.json;

import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author jasonw
 */
public class JSON
{

    ;

    private static SingletonInstance<JSONParser> parser = new SingletonInstance<JSONParser>()
    {

        @Override
        protected JSONParser createInstance()
        {
            return new JSONParser();
        }

    };

    public static JSONObject parseObject( String s )
    {
        try
        {
            s = s.replaceAll( "'", "\"" );
            s = s.replaceAll( "(\\a+) *:", "\"$1\" :" );
            return ( JSONObject ) parser.get().parse( s );
        }
        catch ( ParseException ex )
        {
            Logger.getLogger( JSON.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return new JSONObject();
    }

    public static JSONObject parseArray( String s )
    {
        try
        {
            s = s.replaceAll( "'", "\"" );
            s = s.replaceAll( "(\\w+) *:", "\"$1\" :" );
            return ( JSONObject ) parser.get().parse( s );
        }
        catch ( ParseException ex )
        {
            Logger.getLogger( JSON.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return new JSONObject();
    }

}
