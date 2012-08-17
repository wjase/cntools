/*
 * #%L cntools-animator %% Copyright (C) 2012 Cybernostics Pty Ltd %% Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. #L%
 */

package com.cybernostics.lib.animator.ui.transitions;

import com.cybernostics.lib.patterns.singleton.SingletonInstance;

/**
 *
 * @author jasonw
 */
public abstract class EasingFunction
{

	public static float noEasingFn( float t )
	{
		return t;
	}

	public static SingletonInstance< EasingFunction > linear = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return noEasingFn( t );

				}

			};

		}

	};
	// t: current time, b: begInnIng value, c: change In value, d: duration

	private static final float c = 1f;

	private static final float b = 0f;

	abstract public float map( float t );

	public static float easeInQuadFn( float t )
	{
		return c * ( t ) * t + b;
	}

	public static SingletonInstance< EasingFunction > easeInQuad = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInQuadFn( t );
				}

			};

		}

	};

	public static float easeOutQuadFn( float t )
	{
		return -c * ( t ) * ( t - 2 ) + b;
	}

	public static SingletonInstance< EasingFunction > easeOutQuad = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeOutQuadFn( t );
				}

			};

		}

	};

	public static float easeInOutQuadFn( float t )
	{
		if (( t /= 0.5 ) < 1)
		{
			return c / 2 * t * t + b;
		}
		return -c / 2 * ( ( --t ) * ( t - 2 ) - 1 ) + b;

	}

	public static SingletonInstance< EasingFunction > easeInOutQuad = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInOutQuadFn( t );
				}

			};

		}

	};

	public static float easeInCubicFn( float t )
	{
		return c * ( t ) * t * t + b;
	}

	public static SingletonInstance< EasingFunction > easeInCubic = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInCubicFn( t );
				}

			};

		}

	};

	public static float easeOutCubicFn( float t )
	{
		t -= 1;
		return c * ( t * t * t + 1 ) + b;
	}

	public static SingletonInstance< EasingFunction > easeOutCubic = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeOutCubicFn( t );

				}

			};

		}

	};

	public static float easeInOutCubicFn( float t )
	{
		if (( t /= 0.5 ) < 1)
		{
			return c / 2 * t * t * t + b;
		}
		return c / 2 * ( ( t -= 2 ) * t * t + 2 ) + b;
	}

	public static SingletonInstance< EasingFunction > easeInOutCubic = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInOutCubicFn( t );
				}

			};

		}

	};

	public static float easeInQuartFn( float t )
	{
		return c * ( t ) * t * t * t + b;
	}

	public static SingletonInstance< EasingFunction > easeInQuart = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInQuartFn( t );

				}

			};

		}

	};

	public static float easeOutQuartFn( float t )
	{
		t -= 1;
		return -c * ( t * t * t * t - 1 ) + b;
	}

	public static SingletonInstance< EasingFunction > easeOutQuart = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeOutQuartFn( t );

				}

			};

		}

	};

	public static float easeInOutQuartFn( float t )
	{
		if (( t /= 0.5 ) < 1)
		{
			return c / 2 * t * t * t * t + b;
		}
		return -c / 2 * ( ( t -= 2 ) * t * t * t - 2 ) + b;
	}

	public static SingletonInstance< EasingFunction > easeInOutQuart = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInOutQuartFn( t );

				}

			};

		}

	};

	public static float easeInQuintFn( float t )
	{
		return c * ( t ) * t * t * t * t + b;
	}

	public static SingletonInstance< EasingFunction > easeInQuint = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInQuintFn( t );

				}

			};

		}

	};

	public static float easeOutQuintFn( float t )
	{
		t -= 1;
		return c * ( t * t * t * t * t + 1 ) + b;
	}

	public static SingletonInstance< EasingFunction > easeOutQuint = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeOutQuintFn( t );

				}

			};

		}

	};

	public static float easeInOutQuintFn( float t )
	{
		if (( t / 2 ) < 1)
		{
			return c / 2 * t * t * t * t * t + b;
		}
		t -= 2;
		return c / 2 * ( t * t * t * t * t + 2 ) + b;
	}

	public static SingletonInstance< EasingFunction > easeInOutQuint = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInOutQuintFn( t );

				}

			};

		}

	};

	public static float easeInSineFn( float t )
	{
		return (float) ( -c * Math.cos( t * ( Math.PI / 2 ) ) + c + b );
	}

	public static SingletonInstance< EasingFunction > easeInSine = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInSineFn( t );

				}

			};

		}

	};

	public static float easeOutSineFn( float t )
	{
		return (float) ( c * Math.sin( t * ( Math.PI / 2 ) ) + b );
	}

	public static SingletonInstance< EasingFunction > easeOutSine = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeOutSineFn( t );

				}

			};

		}

	};

	public static float easeInOutSineFn( float t )
	{
		return (float) ( -c / 2 * ( Math.cos( Math.PI * t ) - 1 ) + b );
	}

	public static SingletonInstance< EasingFunction > easeInOutSine = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInOutSineFn( t );

				}

			};

		}

	};

	public static float easeInExpoFn( float t )
	{
		return (float) ( ( t == 0 ) ? b : c * Math.pow(
			2,
			10 * ( t - 1 ) ) + b );
	}

	public static SingletonInstance< EasingFunction > easeInExpo = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInExpoFn( t );

				}

			};

		}

	};

	public static float easeOutExpoFn( float t )
	{
		return (float) ( ( t == 1 ) ? b + c : c * ( -Math.pow(
			2,
			-10 * t ) + 1 ) + b );
	}

	public static SingletonInstance< EasingFunction > easeOutExpo = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeOutExpoFn( t );

				}

			};

		}

	};

	public static float easeInOutExpoFn( float t )
	{
		if (t == 0)
		{
			return b;
		}
		if (t == 1)
		{
			return b + c;
		}
		if (( t /= 0.5 ) < 1)
		{
			return (float) ( c / 2 * Math.pow(
				2,
				10 * ( t - 1 ) ) + b );
		}
		return (float) ( c / 2 * ( -Math.pow(
			2,
			-10 * --t ) + 2 ) + b );
	}

	public static SingletonInstance< EasingFunction > easeInOutExpo = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInOutExpoFn( t );

				}

			};

		}

	};

	public static float easeInCircFn( float t )
	{
		return (float) ( -c * ( Math.sqrt( 1 - ( t ) * t ) - 1 ) + b );
	}

	public static SingletonInstance< EasingFunction > easeInCirc = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInCircFn( t );

				}

			};

		}

	};

	public static float easeOutCircFn( float t )
	{
		t = t - 1;
		return (float) ( c * Math.sqrt( 1 - t * t ) + b );
	}

	public static SingletonInstance< EasingFunction > easeOutCirc = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeOutCircFn( t );

				}

			};

		}

	};

	public static float easeInOutCircFn( float t )
	{
		if (( t /= 0.5 ) < 1)
		{
			return (float) ( -c / 2 * ( Math.sqrt( 1 - t * t ) - 1 ) + b );
		}
		return (float) ( c / 2 * ( Math.sqrt( 1 - ( t -= 2 ) * t ) + 1 ) + b );
	}

	public static SingletonInstance< EasingFunction > easeInOutCirc = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInOutCircFn( t );

				}

			};

		}

	};

	private static float s = 1.70158f;

	public static float easeInElasticFn( float t )
	{
		float p = 0.3f;
		float a = c;
		if (t == 0)
		{
			return b;
		}
		if (( t ) == 1)
		{
			return b + c;
		}
		float u;
		if (a < Math.abs( c ))
		{
			a = c;
			u = p / 4;
		}
		else
		{
			u = (float) ( p / ( 2 * Math.PI ) * Math.asin( c / a ) );
		}
		t -= 1;
		return (float) ( -( a * Math.pow(
			2,
			10 * t ) * Math.sin( ( t - u ) * ( 2 * Math.PI ) / p ) ) + b );
	}

	public static SingletonInstance< EasingFunction > easeInElastic = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInElasticFn( t );

				}

			};

		}

	};

	public static float easeOutElasticFn( float t )
	{
		float p = 0.3f;
		float a = c;
		if (t == 0)
		{
			return b;
		}
		if (( t ) == 1)
		{
			return b + c;
		}
		float u = 0;
		if (a < Math.abs( c ))
		{
			a = c;
			u = p / 4;
		}
		else
		{
			u = (float) ( p / ( 2 * Math.PI ) * Math.asin( c / a ) );
		}

		return (float) ( a * Math.pow(
			2,
			-10 * t ) * Math.sin( ( t - u ) * ( 2 * Math.PI ) / p ) + c + b );
	}

	public static SingletonInstance< EasingFunction > easeOutElastic = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeOutElasticFn( t );

				}

			};

		}

	};

	public static float easeInOutElasticFn( float t )
	{

		float s = 1.70158f;
		float a = c;
		float p = 1f * ( .3f * 1.5f );
		if (t == 0)
		{
			return b;
		}
		if (( t /= 0.5 ) == 2)
		{
			return b + c;
		}

		if (a < Math.abs( c ))
		{
			a = c;
			s = p / 4;
		}
		s = (float) ( p / ( 2 * Math.PI ) * Math.asin( c / a ) );
		if (t < 1)
		{
			return (float) ( -.5
				* ( a * Math.pow(
					2,
					10 * ( t -= 1 ) ) * Math.sin( ( t * 1 - s )
					* ( 2 * Math.PI ) / p ) ) + b );
		}
		return (float) ( a * Math.pow(
			2,
			-10 * ( t -= 1 ) ) * Math.sin( ( t * 1 - s ) * ( 2 * Math.PI ) / p )
			* .5 + c + b );
	}

	public static SingletonInstance< EasingFunction > easeInOutElastic = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInOutElasticFn( t );

				}

			};

		}

	};

	public static float easeInBackFn( float t )
	{
		return c * ( t ) * t * ( ( s + 1 ) * t - s ) + b;
	}

	public static SingletonInstance< EasingFunction > easeInBack = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInBackFn( t );

				}

			};

		}

	};

	public static float easeOutBackFn( float t )
	{

		return c * ( ( t -= 1 ) * t * ( ( s + 1 ) * t + s ) + 1 ) + b;
	}

	public static SingletonInstance< EasingFunction > easeOutBack = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeOutBackFn( t );

				}

			};

		}

	};

	public static float easeInOutBackFn( float t )
	{
		float s = 1.70158f;
		if (( t /= 0.5 ) < 1)
		{
			return c / 2 * ( t * t * ( ( ( s *= ( 1.525 ) ) + 1 ) * t - s ) )
				+ b;
		}
		return c / 2
			* ( ( t -= 2 ) * t * ( ( ( s *= ( 1.525 ) ) + 1 ) * t + s ) + 2 )
			+ b;
	}

	public static SingletonInstance< EasingFunction > easeInOutBack = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInOutBackFn( t );

				}

			};

		}

	};

	public static float easeInBounceFn( float t )
	{
		return 1 - easeOutBounceFn( t );
	}

	public static SingletonInstance< EasingFunction > easeInBounce = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInBounceFn( t );

				}

			};

		}

	};

	public static float easeOutBounceFn( float t )
	{

		if (( t ) < ( 1 / 2.75 ))
		{
			return (float) ( c * ( 7.5625 * t * t ) + b );
		}
		else
			if (t < ( 2 / 2.75 ))
			{
				return (float) ( c
					* ( 7.5625 * ( t -= ( 1.5 / 2.75 ) ) * t + .75 ) + b );
			}
			else
				if (t < ( 2.5 / 2.75 ))
				{
					return (float) ( c
						* ( 7.5625 * ( t -= ( 2.25 / 2.75 ) ) * t + .9375 ) + b );
				}
				else
				{
					return (float) ( c
						* ( 7.5625 * ( t -= ( 2.625 / 2.75 ) ) * t + .984375 ) + b );
				}

	}

	public static SingletonInstance< EasingFunction > easeOutBounce = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeOutBounceFn( t );

				}

			};

		}

	};

	public static float easeInOutBounceFn( float t )
	{
		if (t < 0.5)
		{
			return (float) ( easeInBounceFn( t * 2 ) + b );
		}
		return (float) ( easeOutBounceFn( t * 2 - 1 ) + b );

	}

	public static SingletonInstance< EasingFunction > easeInOutBounce = new SingletonInstance< EasingFunction >()
	{

		@Override
		protected EasingFunction createInstance()
		{
			return new EasingFunction()
			{

				@Override
				public float map( float t )
				{
					return easeInOutBounceFn( t );

				}

			};

		}

	};
}
