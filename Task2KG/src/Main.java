import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class Window extends JFrame {
    private BufferedImage image;
    private Graphics2D g2d;

    public Window() {
        setSize(900, 600);
        setVisible(true);
        setTitle("Треугольник");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // Создаем изображение один раз при запуске
        createImage();
        // Получаем Graphics2D объект для рисования на изображении
        g2d = image.createGraphics();
    }

    private void createImage() {
        int width = getWidth();
        int height = getHeight();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Очищаем изображение фоновым цветом
        g2d = image.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);
        Point p1 = new Point(100, 100, Color.RED);
        Point p2 = new Point(100, 500, Color.GREEN);
        Point p3 = new Point(700, 100, Color.BLUE);

        Point p4 = new Point(700, 120, Color.BLUE);
        Point p5 = new Point(120, 500, Color.GREEN);
        Point p6 = new Point(700, 500, Color.RED);

        Point p7 = new Point(300, 200, Color.BLUE);
        Point p8 = new Point(200, 300, Color.BLUE);
        Point p9 = new Point(400, 400, Color.BLACK);

        drawTriangle(p1, p2, p3);
        drawTriangle(p4, p5, p6);
        drawTriangle(p7, p8, p9);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

    public void drawTriangle(Point p1, Point p2, Point p3) {
        int width = getWidth();
        int height = getHeight();

        int minH = Math.min(p1.y, Math.min(p2.y, p3.y));
        int minW = Math.min(p1.x, Math.min(p2.x, p3.x));
        int maxH = Math.max(p1.y, Math.max(p2.y, p3.y)) + 1;
        int maxW = Math.max(p1.x, Math.max(p2.x, p3.x)) + 1;

        int areaTriangle = (p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y));

        for (int y = minH; y < Math.min(maxH, height); y++) {
            for (int x = minW; x < Math.min(maxW, width); x++) {
                Double[] barycentricCord = calculateBarycentricCord(areaTriangle,p1, p2, p3, x, y);

                if (isInsideTriangle(barycentricCord[0],barycentricCord[1],barycentricCord[2])) {
                    Color interpolatedColor = interpolateColor(p1, p2, p3, barycentricCord[0],barycentricCord[1],barycentricCord[2]);
                    image.setRGB(x, y, interpolatedColor.getRGB());
                }
            }
        }
    }
    
    private Double[] calculateBarycentricCord(int area, Point p1, Point p2, Point p3, int x, int y){
        double alpha = ((double) (x * (p2.y - p3.y) + p2.x * (p3.y - y) + p3.x * (y - p2.y))) / area;
        double beta = ((double) (p1.x * (y - p3.y) + x * (p3.y - p1.y) + p3.x * (p1.y - y))) / area;
        double gamma = 1 - alpha - beta;
        Double[] res = new Double[3];
        res[0] = alpha;
        res[1] = beta;
        res[2] = gamma;
        return res;
    }
    private boolean isInsideTriangle(double alpha, double beta, double gamma) {
        return alpha >= 0 && beta >= 0 && gamma >= 0 && alpha + beta + gamma <= 1;
    }

    private Color interpolateColor(Point p1, Point p2, Point p3, double alpha, double beta, double gamma) {
        int red = (int) (alpha * p1.color.getRed() + beta * p2.color.getRed() + gamma * p3.color.getRed());
        int green = (int) (alpha * p1.color.getGreen() + beta * p2.color.getGreen() + gamma * p3.color.getGreen());
        int blue = (int) (alpha * p1.color.getBlue() + beta * p2.color.getBlue() + gamma * p3.color.getBlue());
        return new Color(red, green, blue);
    }

    private static class Point {
        int x, y;
        Color color;

        public Point(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Window();
        });
    }
}
