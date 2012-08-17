package com.cybernostics.lib.concurrent;

import com.cybernostics.lib.collections.IterableArray;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jasonw
 */
public class ConcurrentSetTest
{

	public ConcurrentSetTest()
	{
	}

	@BeforeClass
	public static void setUpClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownClass() throws Exception
	{
	}

	/**
	 * Test of iterator method, of class ConcurrentSet.
	 */
	@Test
	public void testIterator()
	{
		String[] inSet =
		{ "bob", "john", "paul", "ringo", "tim", "bill" };

		String[] notInSet =
		{ "danny", "melvin", "ricko" };
		ConcurrentSet< String > values = new ConcurrentSet< String >();
		values.addAll( inSet );

		for (String eachOne : IterableArray.get( inSet ))
		{
			Assert.assertTrue( values.contains( eachOne ) );
		}

		for (String eachOne : IterableArray.get( notInSet ))
		{
			Assert.assertTrue( !values.contains( eachOne ) );
		}

		Assert.assertTrue( values.size() == 6 );

		values.remove( "bob" );

		Assert.assertTrue( values.size() == 5 );

		Assert.assertTrue( !values.contains( "bob" ) );

		values.add( "bob" );

		Assert.assertTrue( values.size() == 6 );
		Assert.assertTrue( values.contains( "bob" ) );

		//        for(String eachone:values)
		//        {
		//            System.out.printf( "%\n" );
		//        }
		//        System.out.println( "iterator" );
		//        ConcurrentSet instance = new ConcurrentSet();
		//        Iterator expResult = null;
		//        Iterator result = instance.iterator();
		//        assertEquals( expResult, result );
		//        // TODO review the generated test code and remove the default call to fail.
		//        fail( "The test case is a prototype." );
	}

	//    /**
	//     * Test of toArray method, of class ConcurrentSet.
	//     */
	//    @Test
	//    public void testToArray_0args()
	//    {
	//        System.out.println( "toArray" );
	//        ConcurrentSet instance = new ConcurrentSet();
	//        Object[] expResult = null;
	//        Object[] result = instance.toArray();
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//    /**
	//     * Test of toArray method, of class ConcurrentSet.
	//     */
	//    @Test
	//    public void testToArray_GenericType()
	//    {
	//        System.out.println( "toArray" );
	//        T[] a = null;
	//        ConcurrentSet instance = new ConcurrentSet();
	//        Object[] expResult = null;
	//        Object[] result = instance.toArray( a );
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//    /**
	//     * Test of add method, of class ConcurrentSet.
	//     */
	//    @Test
	//    public void testAdd()
	//    {
	//        System.out.println( "add" );
	//        Object e = null;
	//        ConcurrentSet instance = new ConcurrentSet();
	//        boolean expResult = false;
	//        boolean result = instance.add( e );
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of remove method, of class ConcurrentSet.
	//     */
	//    @Test
	//    public void testRemove()
	//    {
	//        System.out.println( "remove" );
	//        Object o = null;
	//        ConcurrentSet instance = new ConcurrentSet();
	//        boolean expResult = false;
	//        boolean result = instance.remove( o );
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//    /**
	//     * Test of containsAll method, of class ConcurrentSet.
	//     */
	//    @Test
	//    public void testContainsAll()
	//    {
	//        System.out.println( "containsAll" );
	//        Collection<?> c = null;
	//        ConcurrentSet instance = new ConcurrentSet();
	//        boolean expResult = false;
	//        boolean result = instance.containsAll( c );
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of addAll method, of class ConcurrentSet.
	//     */
	//    @Test
	//    public void testAddAll()
	//    {
	//        System.out.println( "addAll" );
	//        Collection<? extends valueType> c = null;
	//        ConcurrentSet instance = new ConcurrentSet();
	//        boolean expResult = false;
	//        boolean result = instance.addAll( c );
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of retainAll method, of class ConcurrentSet.
	//     */
	//    @Test
	//    public void testRetainAll()
	//    {
	//        System.out.println( "retainAll" );
	//        Collection<?> c = null;
	//        ConcurrentSet instance = new ConcurrentSet();
	//        boolean expResult = false;
	//        boolean result = instance.retainAll( c );
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of removeAll method, of class ConcurrentSet.
	//     */
	//    @Test
	//    public void testRemoveAll()
	//    {
	//        System.out.println( "removeAll" );
	//        Collection<?> c = null;
	//        ConcurrentSet instance = new ConcurrentSet();
	//        boolean expResult = false;
	//        boolean result = instance.removeAll( c );
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of size method, of class ConcurrentSet.
	//     */
	//    @Test
	//    public void testSize()
	//    {
	//        System.out.println( "size" );
	//        ConcurrentSet instance = new ConcurrentSet();
	//        int expResult = 0;
	//        int result = instance.size();
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of isEmpty method, of class ConcurrentSet.
	//     */
	//    @Test
	//    public void testIsEmpty()
	//    {
	//        System.out.println( "isEmpty" );
	//        ConcurrentSet instance = new ConcurrentSet();
	//        boolean expResult = false;
	//        boolean result = instance.isEmpty();
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of contains method, of class ConcurrentSet.
	//     */
	//    @Test
	//    public void testContains()
	//    {
	//        System.out.println( "contains" );
	//        Object o = null;
	//        ConcurrentSet instance = new ConcurrentSet();
	//        boolean expResult = false;
	//        boolean result = instance.contains( o );
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of clear method, of class ConcurrentSet.
	//     */
	//    @Test
	//    public void testClear()
	//    {
	//        System.out.println( "clear" );
	//        ConcurrentSet instance = new ConcurrentSet();
	//        instance.clear();
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
}
