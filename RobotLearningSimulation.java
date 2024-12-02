import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

class Robot {
    private double position;  // Posición actual del robot
    private double reward;    // Recompensa obtenida
    private double policyParam;   // Parámetro de política
    private double learningRate;  // Tasa de aprendizaje

    private static final double CLIP_VALUE = 0.2;  // Valor de límite para ajustes
    static final double GOAL_POSITION = 2.0;  // Posición objetivo
        static final double PENALTY_ZONE = 3.0;   // Zona penalizada
            
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
                    if (Math.abs(position - GOAL_POSITION) < 0.5) {
                        return 2.0;  // Recompensa por alcanzar el objetivo
                    } else if (Math.abs(position) > PENALTY_ZONE) {
                        return -2.0; // Penalización por entrar a la zona penalizada
                    } else {
                        return -0.1; // Recompensa baja por no estar en el objetivo
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
            
            public class RobotLearningSimulation extends JFrame {
                private Robot robot = new Robot();
                private JPanel canvas;
                private JLabel infoLabel;
                private Timer timer;
            
                public RobotLearningSimulation() {
                    setTitle("Sistema de Aprendizaje de Robot");
                    setSize(800, 600);
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    setLayout(new BorderLayout());
            
                    // Área de simulación
                    canvas = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            int width = getWidth();
                            int height = getHeight();
            
                            // Dibujar la zona objetivo
                            g.setColor(Color.GREEN);
                            int goalX = (int) (width / 2 + Robot.GOAL_POSITION * 100);
                        g.fillRect(goalX - 15, height / 2 - 50, 30, 100);
        
                        // Dibujar la zona penalizada
                        g.setColor(Color.RED);
                        int penaltyStartX = (int) (width / 2 + Robot.PENALTY_ZONE * 100);
                g.fillRect(penaltyStartX - 10, 0, 20, height);

                // Dibujar el robot
                g.setColor(Color.BLUE);
                int robotX = (int) (width / 2 + robot.getPosition() * 100);
                g.fillOval(robotX - 10, height / 2 - 10, 20, 20);
            }
        };
        canvas.setBackground(Color.WHITE);
        add(canvas, BorderLayout.CENTER);

        // Información de simulación
        infoLabel = new JLabel("Presiona 'Iniciar' para comenzar la simulación.", JLabel.CENTER);
        add(infoLabel, BorderLayout.NORTH);

        // Controles
        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Iniciar");
        JButton stopButton = new JButton("Detener");
        stopButton.setEnabled(false);

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Acciones de botones
        startButton.addActionListener(e -> {
            startSimulation();
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        });

        stopButton.addActionListener(e -> {
            stopSimulation();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        });

        // Temporizador para actualizar la simulación
        timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                robot.move();
                robot.updatePolicy();
                updateInfoLabel();
                canvas.repaint();
            }
        });
    }

    private void startSimulation() {
        timer.start();
    }

    private void stopSimulation() {
        timer.stop();
    }

    private void updateInfoLabel() {
        infoLabel.setText(String.format("Posición: %.2f | Recompensa: %.2f | Parámetro Política: %.2f | Tasa Aprendizaje: %.2f",
                robot.getPosition(), robot.getReward(), robot.getPolicyParam(), robot.getLearningRate()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RobotLearningSimulation frame = new RobotLearningSimulation();
            frame.setVisible(true);
        });
    }
}
