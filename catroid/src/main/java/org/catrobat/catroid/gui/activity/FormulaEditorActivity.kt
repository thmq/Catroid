/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.gui.activity

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import org.catrobat.catroid.R
import org.catrobat.catroid.formula.FormulaInterpreter
import org.catrobat.catroid.formula.Token
import org.catrobat.catroid.formula.operator.BinaryOperatorToken
import org.catrobat.catroid.formula.operator.OperatorToken
import org.catrobat.catroid.formula.textprovider.FormulaTextProvider
import org.catrobat.catroid.formula.value.ValueToken

class FormulaEditorActivity : AppCompatActivity() {

    private val tokens = ArrayList<Token>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formula_editor)

        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar?)
        supportActionBar?.setTitle(R.string.formula_editor_title)
    }

    override fun onResume() {
        super.onResume()

        findViewById(R.id.edit_text)?.setOnTouchListener { v, event ->
            val imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            val inputField = v as EditText

            val position = FormulaTextProvider(resources)
                    .getEndOfTokenStringAtPosition(tokens, inputField.getOffsetForPosition(event.x, event.y))

            inputField.setSelection(position)
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.formula_editor_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.btnSave -> {
            finish()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    fun handleEditorButton(view: View) {

        val inputLayout = (findViewById(R.id.input) as TextInputLayout)
        val inputField = inputLayout.editText

        inputLayout.error = null
        inputLayout.hint = null

        val cursorPosition = inputField?.selectionStart ?: 0

        when (view.id) {
            R.id.btn0 -> appendDigit(cursorPosition, '0')
            R.id.btn1 -> appendDigit(cursorPosition, '1')
            R.id.btn2 -> appendDigit(cursorPosition, '2')
            R.id.btn3 -> appendDigit(cursorPosition, '3')
            R.id.btn4 -> appendDigit(cursorPosition, '4')
            R.id.btn5 -> appendDigit(cursorPosition, '5')
            R.id.btn6 -> appendDigit(cursorPosition, '6')
            R.id.btn7 -> appendDigit(cursorPosition, '7')
            R.id.btn8 -> appendDigit(cursorPosition, '8')
            R.id.btn9 -> appendDigit(cursorPosition, '9')

            R.id.btnDecimal -> appendDigit(cursorPosition, '.')

            R.id.btnAdd -> addToken(cursorPosition, BinaryOperatorToken.AddOperatorToken())
            R.id.btnSub -> addToken(cursorPosition, BinaryOperatorToken.SubOperatorToken())
            R.id.btnMult -> addToken(cursorPosition, BinaryOperatorToken.MultOperatorToken())
            R.id.btnDiv -> addToken(cursorPosition, BinaryOperatorToken.DivOperatorToken())

            R.id.btnBracketOpen -> addToken(cursorPosition,
                    OperatorToken.BracketOperator(Token.Type.LEFT_BRACKET))
            R.id.btnBracketClose -> addToken(cursorPosition,
                    OperatorToken.BracketOperator(Token.Type.RIGHT_BRACKET))

            R.id.btnEquals -> addToken(cursorPosition, BinaryOperatorToken.EqualsOperatorToken())

            R.id.btnBack -> if (tokens.isNotEmpty()) {
                removeToken(FormulaTextProvider(resources).getTokenAtPosition(tokens, cursorPosition))
            }

            R.id.btnCompute -> {
                try {
                    inputLayout.hint = FormulaInterpreter().eval(tokens).getString()
                } catch (exception: Exception) {
                    inputLayout.error = getString(R.string.fe_parser_error)
                }
            }
        }

        val formulaText = FormulaTextProvider(resources).getText(tokens)
        val cursorOffset = formulaText.length - (inputField?.text?.length ?: 0)

        inputField?.setText(formulaText)
        inputField?.setSelection(cursorPosition + cursorOffset)
    }

    private fun appendDigit(cursorPosition: Int, digit: Char) {
        if (cursorPosition == 0) tokens.add(0, ValueToken(0.0))

        val textProvider = FormulaTextProvider(resources)
        var tokenToEdit = textProvider.getTokenAtPosition(tokens, cursorPosition)

        if (tokenToEdit !is ValueToken) {
            tokenToEdit = ValueToken(0.0)
            addToken(cursorPosition, tokenToEdit)
        }

        (tokenToEdit).appendDigit(digit)
    }

    private fun addToken(cursorPosition: Int, token: Token) {
        if (cursorPosition == 0) tokens.add(0, token) else {
            val tokenAtPosition = FormulaTextProvider(resources).getTokenAtPosition(tokens, cursorPosition)
            tokens.add(tokens.indexOf(tokenAtPosition) + 1, token)
            sanitizeTokenList()
        }
    }

    private fun sanitizeTokenList() {
        val saneTokenList = ArrayList<Token>()

        for (token in tokens) {
            when (token) {
                tokens.first() -> saneTokenList.add(token)
                is BinaryOperatorToken.MissingBinaryOperatorToken -> {
                    if (tokens[tokens.indexOf(token)] !is BinaryOperatorToken.MissingBinaryOperatorToken) {
                        saneTokenList.add(token)
                    }
                }
                else -> saneTokenList.add(token)
            }
        }

        tokens.clear()
        tokens.addAll(saneTokenList)
    }

    private fun removeToken(token: Token) {
        when (token) {
            is ValueToken -> if (token.removeDigit()) tokens.remove(token)
            is BinaryOperatorToken -> removeBinaryOperatorToken(token)
            else -> tokens.remove(token)
        }
    }

    private fun removeBinaryOperatorToken(token: BinaryOperatorToken) {
        when {
            token == tokens.last() ||
            token == tokens.first() ||
            tokens[tokens.indexOf(token) - 1] is BinaryOperatorToken ||
            tokens[tokens.indexOf(token) + 1] is BinaryOperatorToken -> {
                tokens.remove(token)
            }
            else -> tokens[tokens.indexOf(token)] = BinaryOperatorToken.MissingBinaryOperatorToken()
        }
    }
}
