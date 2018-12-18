/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends IterativeRobot {

    //Drive Train
    Talon frontLeft = new Talon(8);
    Talon frontRight = new Talon(0);
    Talon rearLeft = new Talon(7);
    Talon rearRight  = new Talon(1);
    SpeedController leftSide = new SpeedControllerGroup(frontLeft, rearLeft);
    SpeedController rightSide = new SpeedControllerGroup(frontRight, rearRight);
    DifferentialDrive driveTrain = new DifferentialDrive(leftSide, rightSide);

    //Pick up
    Talon pickUpLeft = new Talon(6);
    Talon pickUpRight = new Talon(5);
    Talon pickUpMover = new Talon(4);

    //Shooter release
    Talon release = new Talon(9);
    
    //Shooter pull down
    Talon puller = new Talon(2);
    Talon puller2 = new Talon(3);
    DigitalInput stringEncoder = new DigitalInput(0);
    boolean previousInput = false;
    int count = 0;
    boolean inplace = false;
    DigitalInput armDown = new DigitalInput(1);
    
    //PDP
    PowerDistributionPanel pdp = new PowerDistributionPanel();

    //Xbox Controller
    XboxController driveStick = new XboxController(0);

    @Override
    public void teleopPeriodic(){

        driveTrain.arcadeDrive(-driveStick.getRawAxis(1), driveStick.getRawAxis(4));

        if(driveStick.getStartButton()){

            pickUpLeft.set(1);
            pickUpRight.set(-1);

        }
        else if(driveStick.getBackButton()){
            
            pickUpLeft.set(-1);
            pickUpRight.set(1);

        }
        else{

            pickUpLeft.set(0);
            pickUpRight.set(0);

        }

        if(driveStick.getBButton() && armDown.get()){
            
            if(previousInput != stringEncoder.get()){
                
                previousInput = stringEncoder.get();
                count++;

            }
            
            puller.set(0.3);
            puller2.set(0.3);

        }
        else if(count > 0){
            
            if(previousInput != stringEncoder.get()){
                
                previousInput = stringEncoder.get();
                count--;

            }

            puller.set(-0.3);
            puller2.set(-0.3);

        }
        else{

            puller.set(0);
            puller2.set(0);

        }

        if(driveStick.getAButton()){
            
            release.set(-1);
            inplace = false;
        }
        else if(!inplace){
            
            release.set(0.4);
            
            if(pdp.getCurrent(11) > 3){
                
                inplace = true;
            
            }

        }
        else{
            
            release.set(0);

        }

        if(driveStick.getStickButton(Hand.kLeft)){
            
            pickUpMover.set(-0.75);
        
        }
        else if(driveStick.getStickButton(Hand.kRight)){
            
            pickUpMover.set(.75);
        
        }
        else{
            
            pickUpMover.set(0);

        }
        
    } 

}