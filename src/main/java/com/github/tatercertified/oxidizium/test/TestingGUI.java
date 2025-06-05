package com.github.tatercertified.oxidizium.test;

import com.github.tatercertified.oxidizium.Oxidizium;
import imgui.ImColor;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class TestingGUI extends Application {

    private static boolean ran;
    private static final Set<String> FAILED_TESTS = new HashSet<>();
    private static volatile String currentTest = "";
    private static volatile String currentClass = "";
    private static volatile String currentMethod = "";
    private static final AtomicInteger currentRun = new AtomicInteger(0);
    private static final AtomicInteger TOTAL_TESTS = new AtomicInteger(0);
    private static final AtomicInteger CURRENT_TEST_INDEX = new AtomicInteger(0);

    public static void setCurrentTestName(String name) {
        currentTest = name;
    }

    public static void setCurrentClass(String className) {
        currentClass = className + ".class";
    }

    public static void setCurrentMethod(String methodAndParams) {
        currentMethod = methodAndParams;
        CURRENT_TEST_INDEX.incrementAndGet();
    }

    public static void setCurrentRun(int run) {
        currentRun.set(run);
    }

    public static void setTotalTests(int tests) {
        TOTAL_TESTS.set(tests);
    }

    public static void addError(String methodName, boolean warning, String... warningInfo) {
        if (warning) {
            FAILED_TESTS.add(methodName + ": " + warningInfo[0]);
        } else {
            FAILED_TESTS.add("%RED%" + methodName + ": " + warningInfo[0]);
        }
    }

    @Override
    protected void configure(Configuration config) {
        config.setTitle("Oxidizium Native Testing");
        config.setHeight(600);
        config.setWidth(600);
        super.configure(config);
    }

    @Override
    protected void preRun() {
        Oxidizium.TEST_LOGGER.info("Starting Native Testing");
        NativeTest.prepareTests();
        super.preRun();
    }

    @Override
    protected void initWindow(Configuration config) {
        super.initWindow(config);
    }

    @Override
    public void process() {
        // Set position and size before Begin
        ImGui.setNextWindowPos(0, 0);
        ImGui.setNextWindowSize(600, 600);

        int windowFlags = ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize |
                ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoTitleBar;

        ImGui.begin("Oxidizium Native Tester", windowFlags);

        // Padding and layout constants
        float padding = 20.0f;
        float buttonHeight = 30.0f;
        float progressBarHeight = 20.0f;
        float spacing = 10.0f;

        float windowHeight = ImGui.getWindowSizeY();
        float windowWidth = ImGui.getWindowSizeX();

        // Box dimensions
        float boxWidth = 250.0f;
        float boxHeight = 400.0f;

        // ---------------- Left Box (Errors) ----------------

        ImGui.setCursorPosX(padding + boxWidth / 2 - ImGui.calcTextSizeX("Errors") / 2);
        ImGui.setCursorPosY(padding);
        ImGui.text("Errors");

        ImGui.setCursorPosX(padding);
        ImGui.setCursorPosY(padding * 3);

        ImGui.pushStyleColor(ImGuiCol.ChildBg, 0xFF2E2E2E); // dark gray background

        ImGui.beginChild("LogBox", boxWidth, boxHeight, true, ImGuiWindowFlags.HorizontalScrollbar);
        for (String line : FAILED_TESTS) {
            if (line.contains("%RED%")) {
                ImGui.textColored(ImColor.rgb(255, 0, 0), line.replace("%RED%", ""));
            } else {
                ImGui.text(line);
            }
        }
        ImGui.endChild();

        ImGui.popStyleColor();

        // ---------------- Right Box (Test Info) ----------------

        float rightBoxX = windowWidth - padding - boxWidth;

        ImGui.setCursorPosX(rightBoxX + boxWidth / 2 - ImGui.calcTextSizeX("Info") / 2);
        ImGui.setCursorPosY(padding);
        ImGui.text("Info");

        ImGui.setCursorPosX(rightBoxX);
        ImGui.setCursorPosY(padding * 3);

        ImGui.pushStyleColor(ImGuiCol.ChildBg, 0xFF2E2E2E); // same gray

        ImGui.beginChild("InfoBox", boxWidth, boxHeight, true, ImGuiWindowFlags.HorizontalScrollbar);

        centerText("Test Name", boxWidth, true);
        multiSpace(4);
        centerText(currentTest, boxWidth, false);
        multiSpace(6);
        centerText("Class", boxWidth, true);
        multiSpace(4);
        centerText(currentClass, boxWidth, false);
        multiSpace(6);
        centerText("Method", boxWidth, true);
        multiSpace(4);
        centerText(currentMethod, boxWidth, false);
        multiSpace(14);
        centerText("Run: " + currentRun.get(), boxWidth, false);

        ImGui.endChild();

        ImGui.popStyleColor();

        // ---------------- Bottom Button and Progress Bar ----------------

        float totalHeight = progressBarHeight + spacing + buttonHeight + 2 * padding;
        float startY = windowHeight - totalHeight;

        ImGui.setCursorPosY(startY + padding);

        float buttonWidth = 150.0f;
        ImGui.setCursorPosX((windowWidth - buttonWidth) / 2);
        if (!ran && ImGui.button("Start Tests", buttonWidth, buttonHeight)) {
            new Thread(NativeTest::invokeTests).start();
            ran = true;
        }

        ImGui.setCursorPosY(ImGui.getCursorPosY() + spacing);

        float progress = CURRENT_TEST_INDEX.floatValue() / TOTAL_TESTS.floatValue();
        float progressBarWidth = 300.0f;
        ImGui.setCursorPosX((windowWidth - progressBarWidth) / 2);
        ImGui.progressBar(progress, progressBarWidth, progressBarHeight);

        ImGui.end();
    }

    private static void centerText(String text, float windowWidth, boolean underline) {
        ImGui.setCursorPosX(windowWidth / 2 - ImGui.calcTextSizeX(text) / 2);
        if (underline) {
            underlinedText(text);
        } else {
            ImGui.text(text);
        }
    }

    private static void underlinedText(String text) {
        ImGui.text(text);

        // Get draw list and current cursor position
        ImDrawList drawList = ImGui.getWindowDrawList();
        float textWidth = ImGui.calcTextSizeX(text);
        float textHeight = ImGui.getFontSize();

        // Get top-left corner of the text
        float textStartX = ImGui.getItemRectMinX();
        float textStartY = ImGui.getItemRectMinY();

        // Underline 1px below the text
        float underlineY = textStartY + textHeight;

        // Draw the underline
        drawList.addLine(
                textStartX, underlineY,
                textStartX + textWidth, underlineY,
                ImGui.getColorU32(ImGuiCol.Text) // Match text color
        );
    }

    private static void multiSpace(int times) {
        for (int i = 0; i < times; i++) {
            ImGui.spacing();
        }
    }
}
