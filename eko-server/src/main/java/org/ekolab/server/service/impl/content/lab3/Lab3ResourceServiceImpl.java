package org.ekolab.server.service.impl.content.lab3;

import com.twelvemonkeys.image.ImageUtil;
import com.twelvemonkeys.image.ResampleOp;
import org.ekolab.server.dev.LogExecutionTime;
import org.ekolab.server.model.content.lab3.City;
import org.ekolab.server.model.content.lab3.WindDirection;
import org.ekolab.server.service.api.content.lab3.Lab3ResourceService;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 777Al on 24.04.2017.
 */
@Service
public class Lab3ResourceServiceImpl implements Lab3ResourceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Lab3ResourceServiceImpl.class);

    private final Image COMPASS_ARROW = loadImage("map/compass-arrow.svg", 0.5);

    private final Map<City, Image> BACKGROUND_CACHE = new HashMap<>(City.values().length);

    @Override
    @Cacheable("WIND_ROSE_URL_CACHE")
    public URL getWindRose(City city) {
        return Lab3ResourceServiceImpl.class.getResource("wind/" + city.name() + ".svg");
    }

    @Override
    @Cacheable("WIND_ROSE_CACHE")
    @LogExecutionTime(200)
    public Image getWindRoseImage(City city) {
        try (InputStream is = getWindRose(city).openStream()){
            return new ResampleOp(150, 150, ResampleOp.FILTER_LANCZOS).filter(ImageIO.read(is), null);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    @Cacheable("BACKGROUND_CACHE")
    @LogExecutionTime(200)
    public Image getBackgroundImage(City city, WindDirection windDirection) {
        Image background = BACKGROUND_CACHE.computeIfAbsent(city, cityName -> loadImage("map/" + city.name() + ".png", -1.0));

        double angle = Math.PI + windDirection.ordinal() * (Math.PI / 4.0);
        BufferedImage rotatedBackground = ImageUtil.createRotated(background, angle);
        rotatedBackground = rotatedBackground.getSubimage(rotatedBackground.getWidth() / 2, rotatedBackground.getHeight() / 2 - rotatedBackground.getWidth() / 8,
                rotatedBackground.getWidth() / 4, rotatedBackground.getWidth() / 4);
        BufferedImage copyOfImage = new BufferedImage(rotatedBackground.getWidth(), rotatedBackground.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(rotatedBackground, 0, 0, null);
        BufferedImage rotatedArrow = ImageUtil.createRotated(COMPASS_ARROW, angle);
        Graphics2D g2d = copyOfImage.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g2d.drawImage(rotatedArrow, 0, 0, null);
        g2d.dispose();

        try (InputStream is = new ByteArrayInputStream(EncoderUtil.encode(rotatedBackground, ImageFormat.PNG, 0.2f, true))) {
            return ImageIO.read(is);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Image loadImage(String imageName, double scale) {
        try (InputStream is = Lab3ResourceServiceImpl.class.getResourceAsStream(imageName)) {
            Image i = ImageIO.read(is);
            return scale == -1.0 ? i : i.getScaledInstance((int) Math.round(i.getWidth(null) * scale), (int) Math.round(i.getHeight(null) * scale), Image.SCALE_DEFAULT);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
