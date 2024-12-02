/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package simulacion;

public class Simulacion {

    public static void main(String[] args) {
        // TODO code application logic here
    }
    class Robot {
    private double position;  // Posición actual del robot
    private double reward;    // Recompensa obtenida
    private double policyParam;   // Parámetro de política
    private double learningRate;  // Tasa de aprendizaje

    private static final double CLIP_VALUE = 0.2;  // Valor de límite para ajustes
    private static final double GOAL_START = 1.5;  // Inicio de la zona verde
    private static final double GOAL_END = 2.5;    // Fin de la zona verde
    private static final double PENALTY_START = -3.0; // Inicio de la zona roja
    private static final double PENALTY_END = -2.0;   // Fin de la zona roja

    public Robot() {
        this.position = 0.0;
        this.reward = 0.0;
        this.policyParam = 0.5;
        this.learningRate = 0.1;
    }

    public void move() {
        Random rand = new Random();
        double action = policyParam * (-1 + 2 * rand.nextDouble());
        position += action;
        reward = evaluateMovement();
    }

    private double evaluateMovement() {
        if (position >= GOAL_START && position <= GOAL_END) {
            return 2.0; // Recompensa por estar en la zona verde
        } else if (position >= PENALTY_START && position <= PENALTY_END) {
            return -2.0; // Penalización por estar en la zona roja
        } else if (position < GOAL_START) {
            return 0.5; // Incentivo leve por acercarse a la zona verde
        } else {
            return -0.5; // Penalización leve por alejarse de la zona verde
        }
    }

    public void updatePolicy() {
        double oldPolicyParam = policyParam;
        policyParam += learningRate * reward;

        // Aplicación del clipping
        if (Math.abs(policyParam - oldPolicyParam) > CLIP_VALUE) {
            policyParam = oldPolicyParam + Math.signum(policyParam - oldPolicyParam) * CLIP_VALUE;
        }

        // Ajuste de la tasa de aprendizaje
        if (reward > 0) {
            learningRate *= 1.05;
        } else {
            learningRate *= 0.95;
        }

        learningRate = Math.max(0.01, Math.min(learningRate, 0.5));
    }

    public double getPosition() {
        return position;
    }

    public double getReward() {
        return reward;
    }

    public double getPolicyParam() {
        return policyParam;
    }

    public double getLearningRate() {
        return learningRate;
    }
}

}
