package com.adityapradhan.hybridobstacleavoidance;

import android.util.Log;

import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by aditya on 8/15/16.
 */


import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.Observer;


/*
 * implementation of a basic Kalman Filter using the Apache common
 * KalmanFilter library
 */
public class ObstacleKalmanFilter implements FilterSubject {
    private KalmanFilter filter;
    private ArrayList<FilterObserver> observers = new ArrayList<FilterObserver>();
    private RealVector prediction;
    private RealVector measurement;
    private RealVector estimate;
    private int currentTimeStep = 0;

    // TODO Extra add constructor that allows user to set all of the matrices if they desire
    // TODO Add constructur parameters to be used for X0, Z0;
    public ObstacleKalmanFilter() {
        final double dt = 0.05; // time delta in seconds
        final double velocity = 1; // TODO hard-coded for dev purposes use dynamic velocity later


        // state transition matrix
        RealMatrix A = new Array2DRowRealMatrix(
                new double[][] {
                        { 1, 0, 0, dt },
                        { 0, 1, 0, dt },
                        { 0, 0, 1, dt },
                        { 0, 0, 0, 1  }

                });

        // no control input modelled
        RealMatrix B = null;

        // Measurement function matrix - used to convert state matrix into
        // measurement space to calculate the residual
        // measurement matrix vector fill we in the form:
		/*
		 * [1.23] [1] Z = [1.41] X = [2] [1.67] [3] [4]
		 */
        RealMatrix H = new Array2DRowRealMatrix(new double[][] {
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 1, 0 }

        });

        // process noise covariance matrix
        RealMatrix Q = new Array2DRowRealMatrix(
                new double[][] {
//                        { 0.1, 0, 0, 0 },
//                        { 0, 0.1, 0, 0 },
//                        { 0, 0, 0.1, 0 },
//                        { 0, 0, 0, 0 }
                        { Math.pow(dt,3)/3, 0, 0, 0 },
                        { 0, Math.pow(dt,3)/3, 0, 0 },
                        { 0, 0, Math.pow(dt,3)/3, 0 },
                        { 0, 0, 0, dt               }
                });

        // sensor error covariance matrix
        RealMatrix R = new Array2DRowRealMatrix(new double[][] {
                {47.6 , 0, 0 },
                { 0, 120.8, 0 },
                { 0, 0, 97.7 }

        });


        // Initial state estimate
        RealVector X0 = new ArrayRealVector(new double[] { 1.5, 1.5, 1.5, 1 });

        // initial error covariance matrix
        RealMatrix P0 = new Array2DRowRealMatrix(
                new double[][] {
                        { 9, 0, 0, 0 },
                        { 0, 10, 0, 0 },
                        { 0, 0, 12, 0 },
                        { 0, 0, 0, 1.25 }

                });

        ProcessModel pm = new DefaultProcessModel(A, B, Q, X0, P0);
        MeasurementModel mm = new DefaultMeasurementModel(H, R);
        filter = new KalmanFilter(pm, mm);


        System.out.println("Filter created successfully");

        // mock measurements for x
        double[] z = { 2.3, 2.9, 1.7 };

        // mock measurement vector velocity is constant
        RealVector Z =  new ArrayRealVector(z);
        RealVector[] predictions = new RealVector[3];
        RealVector[] estimates = new RealVector[3];

//        for (int i = 0; i < 1; i++) {
//            System.out.println("Iteration: " + (i + 1));
//            filter.predict();
//            // store prediction and print
//            predictions[i] = filter.getStateEstimationVector();
//            System.out.println("predicted state: " + predictions[0]);
//            System.out.println("error covariance matrix: " + filter.getErrorCovarianceMatrix());
//
//            filter.correct(Z);
//            // store filter estimate and print
//            estimates[i] = filter.getStateEstimationVector();
//            System.out.println("estimated stated: " + estimates[i]);
//
//        }

    }

    public ObstacleKalmanFilter(RealVector initialState) {
        final double dt = 0.05; // time delta
        final double velocity = 1; // TODO hard-coded for dev purposes use dynamic velocity later


        // state transition matrix
        RealMatrix A = new Array2DRowRealMatrix(
                new double[][] {
                        { 1, 0, 0, dt },
                        { 0, 1, 0, dt },
                        { 0, 0, 1, dt },
                        { 0, 0, 0, 1  }

                });

        // no control input modelled
        RealMatrix B = null;

        // Measurement function matrix - used to convert state matrix into
        // measurement space to calculate the residual
        // measurement matrix vector fill we in the form:
		/*
		 * [1.23] [1] Z = [1.41] X = [2] [1.67] [3] [4]
		 */
        RealMatrix H = new Array2DRowRealMatrix(new double[][] {
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 1, 0 }

        });

        // process noise covariance matrix
        RealMatrix Q = new Array2DRowRealMatrix(
                new double[][] {
//                        { 0, 0, 0, 0 },
//                        { 0, 0, 0, 0 },
//                        { 0, 0, 0, 0 },
//                        { 0, 0, 0, 0 }

                        { Math.pow(dt,3)/3, 0, 0, 0 },
                        { 0, Math.pow(dt,3)/3, 0, 0 },
                        { 0, 0, Math.pow(dt,3)/3, 0 },
                        { 0, 0, 0, 0              }
                });

        // sensor error covariance matrix
        RealMatrix R = new Array2DRowRealMatrix(new double[][] {
//                { 47, 0, 0 },
//                { 0, 100, 0 },
//                { 0, 0, 96 }
                {47.6 , 0, 0 },
                { 0, 120.8, 0 },
                { 0, 0, 97.7 }

        });

        // Initial state estimate
        RealVector X0 = initialState;

        // initial error covariance matrix
        RealMatrix P0 = new Array2DRowRealMatrix(
                new double[][] {
                        { 47, 0, 0, 0 },
                        { 0, 100, 0, 0 },
                        { 0, 0, 96, 0 },
                        { 0, 0, 0, 0 }

                });

        ProcessModel pm = new DefaultProcessModel(A, B, Q, X0, P0);
        MeasurementModel mm = new DefaultMeasurementModel(H, R);
        filter = new KalmanFilter(pm, mm);


        System.out.println("Filter created successfully");

        // mock measurements for x
        double[] z = { 2.3, 2.9, 1.7 };

        // mock measurement vector velocity is constant
        RealVector Z =  new ArrayRealVector(z);
        RealVector[] predictions = new RealVector[3];
        RealVector[] estimates = new RealVector[3];

//        for (int i = 0; i < 1; i++) {
//            System.out.println("Iteration: " + (i + 1));
//            filter.predict();
//            // store prediction and print
//            predictions[i] = filter.getStateEstimationVector();
//            System.out.println("predicted state: " + predictions[0]);
//            System.out.println("error covariance matrix: " + filter.getErrorCovarianceMatrix());
//
//            filter.correct(Z);
//            // store filter estimate and print
//            estimates[i] = filter.getStateEstimationVector();
//            System.out.println("estimated stated: " + estimates[i]);
//
//        }

    }

    public ObstacleKalmanFilter(RealVector initialState, RealMatrix initialCovariance) {
        final double dt = 0.05; // time delta
        final double velocity = 1; // TODO hard-coded for dev purposes use dynamic velocity later


        // state transition matrix
        RealMatrix A = new Array2DRowRealMatrix(
                new double[][] {
                        { 1, 0, 0, dt },
                        { 0, 1, 0, dt },
                        { 0, 0, 1, dt },
                        { 0, 0, 0, 1  }

                });

        // no control input modelled
        RealMatrix B = null;

        // Measurement function matrix - used to convert state matrix into
        // measurement space to calculate the residual
        // measurement matrix vector fill we in the form:
		/*
		 * [1.23] [1] Z = [1.41] X = [2] [1.67] [3] [4]
		 */
        RealMatrix H = new Array2DRowRealMatrix(new double[][] {
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 1, 0 }

        });

        // process noise covariance matrix
        RealMatrix Q = new Array2DRowRealMatrix(
                new double[][] {
                        { Math.pow(dt,3)/3, 0, 0, 0 },
                        { 0, Math.pow(dt,3)/3, 0, 0 },
                        { 0, 0, Math.pow(dt,3)/3, 0 },
                        { 0, 0, 0, 0               }
                });

        // sensor error covariance matrix
        RealMatrix R = new Array2DRowRealMatrix(new double[][] {
//                { 47, 0, 0 },
//                { 0, 100, 0 },
//                { 0, 0, 96 }
                {47.6 , 0, 0 },
                { 0, 120.8, 0 },
                { 0, 0, 97.7 }


        });

        // Initial state estimate
        RealVector X0 = initialState;

        // initial error covariance matrix
        RealMatrix P0 = initialCovariance;

        ProcessModel pm = new DefaultProcessModel(A, B, Q, X0, P0);
        MeasurementModel mm = new DefaultMeasurementModel(H, R);
        filter = new KalmanFilter(pm, mm);


        System.out.println("Filter created successfully");

        // mock measurements for x
        double[] z = { 2.3, 2.9, 1.7 };

        // mock measurement vector velocity is constant
        RealVector Z =  new ArrayRealVector(z);
        RealVector[] predictions = new RealVector[3];
        RealVector[] estimates = new RealVector[3];


    }

    public void predict() {
        filter.predict();
    }

    // takes measurement as a vector
    public void correct(RealVector z) {
        Log.i("Filter status", "Filter was corrected");
        prediction = this.getStateEstimationVector(); // stores state vector
        filter.correct(z);
        measurement = z;
        estimate = filter.getStateEstimationVector(); // gets updated state vector
        currentTimeStep += 50; // 50 ms increments
        notifyFilterObservers();

    }



    public RealVector getStateEstimationVector() {
        return filter.getStateEstimationVector();
    }

    public RealMatrix getStateCovarianceMatrix() {
        return filter.getErrorCovarianceMatrix();
    }

    @Override
    public void registerObserver(FilterObserver o) {
        observers.add(o);
    }

    @Override
    public void removeFilterObserver(FilterObserver o) {
        int i = observers.indexOf(o);
        if (i >= 0) {
            observers.remove(i);
        }

    }

    // notifies all registered Observers with the vectors representing the prediction, measurement and estimated state vector after taking into account the measurement
    @Override
    public void notifyFilterObservers() {
        for(int i = 0; i < observers.size(); i++) {
            FilterObserver observer = observers.get(i);
            observer.onFilterUpdate(prediction, measurement, estimate, currentTimeStep);
        }
    }
}
